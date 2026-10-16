const store = require('../store');

async function orderRoutes(fastify) {
  // Create order (cashier)
  fastify.post('/orders', { preHandler: [fastify.authenticate] }, async (request, reply) => {
    const { items } = request.body || {};
    if (!Array.isArray(items) || items.length === 0) {
      return reply.code(400).send({ error: 'items required' });
    }
    const orderItems = [];
    let total = 0;
    for (const it of items) {
      const menu = store.menu.find(m => m.id === it.menuId);
      if (!menu) return reply.code(400).send({ error: 'unknown menu item ' + it.menuId });
      const qty = Number(it.qty) || 1;
      if (qty <= 0) return reply.code(400).send({ error: 'invalid qty' });
      const line = { menuId: menu.id, name: menu.name, price: menu.price, qty };
      line.subtotal = menu.price * qty;
      total += line.subtotal;
      orderItems.push(line);
    }
    const order = {
      id: store.nextId('o'),
      items: orderItems,
      total,
      status: 'pending', // pending -> preparing -> ready -> completed
      createdAt: new Date().toISOString(),
      cashierId: request.user.id
    };
    store.orders.push(order);
    return reply.code(201).send(order);
  });

  fastify.get('/orders', async (request) => {
    const status = request.query && request.query.status;
    const list = status ? store.orders.filter(o => o.status === status) : store.orders.slice();
    return { orders: list };
  });

  fastify.get('/orders/:id', async (request, reply) => {
    const order = store.orders.find(o => o.id === request.params.id);
    if (!order) return reply.code(404).send({ error: 'not found' });
    return order;
  });

  // Update status (kitchen / cashier)
  fastify.patch('/orders/:id/status', { preHandler: [fastify.authenticate] }, async (request, reply) => {
    const order = store.orders.find(o => o.id === request.params.id);
    if (!order) return reply.code(404).send({ error: 'not found' });
    const { status } = request.body || {};
    const allowed = ['pending', 'preparing', 'ready', 'completed', 'cancelled'];
    if (!allowed.includes(status)) return reply.code(400).send({ error: 'invalid status' });
    order.status = status;
    order.updatedAt = new Date().toISOString();
    if (status === 'completed') {
      store.transactions.push({ id: store.nextId('t'), orderId: order.id, total: order.total, createdAt: new Date().toISOString() });
    }
    return order;
  });
}

module.exports = orderRoutes;
