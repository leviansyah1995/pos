const test = require('node:test');
const assert = require('node:assert');
const { build } = require('../src/server');
const store = require('../src/store');

let app;
test.before(async () => {
  app = await build();
  await app.ready();
  store.reset();
});
test.after(async () => { await app.close(); });

async function login(username, password) {
  const res = await app.inject({ method: 'POST', url: '/auth/login', payload: { username, password } });
  assert.equal(res.statusCode, 200);
  return JSON.parse(res.body).token;
}

test('health', async () => {
  const res = await app.inject({ method: 'GET', url: '/health' });
  assert.equal(res.statusCode, 200);
});

test('login invalid', async () => {
  const res = await app.inject({ method: 'POST', url: '/auth/login', payload: { username: 'x', password: 'y' } });
  assert.equal(res.statusCode, 401);
});

test('menu list', async () => {
  const res = await app.inject({ method: 'GET', url: '/menu' });
  assert.equal(res.statusCode, 200);
  const body = JSON.parse(res.body);
  assert.ok(Array.isArray(body.items));
  assert.ok(body.items.length >= 1);
});

test('create order flow', async () => {
  store.reset();
  const token = await login('kasir', 'kasir123');
  const menu = JSON.parse((await app.inject({ method: 'GET', url: '/menu' })).body).items;
  const res = await app.inject({
    method: 'POST', url: '/orders',
    headers: { authorization: 'Bearer ' + token },
    payload: { items: [{ menuId: menu[0].id, qty: 2 }] }
  });
  assert.equal(res.statusCode, 201);
  const order = JSON.parse(res.body);
  assert.equal(order.status, 'pending');
  assert.equal(order.total, menu[0].price * 2);

  // kitchen updates
  const ktoken = await login('dapur', 'dapur123');
  const r2 = await app.inject({ method: 'PATCH', url: '/orders/' + order.id + '/status', headers: { authorization: 'Bearer ' + ktoken }, payload: { status: 'preparing' } });
  assert.equal(r2.statusCode, 200);
  const r3 = await app.inject({ method: 'PATCH', url: '/orders/' + order.id + '/status', headers: { authorization: 'Bearer ' + ktoken }, payload: { status: 'completed' } });
  assert.equal(r3.statusCode, 200);
  assert.equal(JSON.parse(r3.body).status, 'completed');
});

test('unauthorized order create', async () => {
  const res = await app.inject({ method: 'POST', url: '/orders', payload: { items: [{ menuId: 'm1', qty: 1 }] } });
  assert.equal(res.statusCode, 401);
});
