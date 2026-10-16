let token = localStorage.getItem('token') || '';
let cart = [];
const $ = (id) => document.getElementById(id);

function authHeaders() {
  return token ? { Authorization: 'Bearer ' + token, 'Content-Type': 'application/json' } : { 'Content-Type': 'application/json' };
}

async function api(path, opts = {}) {
  const res = await fetch(path, { ...opts, headers: { ...authHeaders(), ...(opts.headers || {}) } });
  if (!res.ok) {
    const e = await res.json().catch(() => ({}));
    throw new Error(e.error || res.statusText);
  }
  if (res.status === 204) return null;
  return res.json();
}

async function login() {
  try {
    const data = await api('/auth/login', { method: 'POST', body: JSON.stringify({ username: $('username').value, password: $('password').value }) });
    token = data.token;
    localStorage.setItem('token', token);
    $('who').textContent = data.user.username + ' (' + data.user.role + ')';
    await loadAll();
  } catch (e) { alert(e.message); }
}

function show(name) {
  document.querySelectorAll('.view').forEach(v => v.style.display = 'none');
  $(name).style.display = 'block';
  if (name === 'kitchen') loadKitchen();
}

async function loadMenu() {
  const data = await api('/menu');
  const list = data.items;
  $('menuList').innerHTML = list.map(m => `
    <div class="card">
      <b>${m.name}</b><br/>${m.category}<br/>Rp ${m.price.toLocaleString('id-ID')}
      <br/><button onclick="addToCart('${m.id}')">+ Keranjang</button>
    </div>`).join('');
  $('menuManage').innerHTML = list.map(m => `<div>${m.name} - Rp ${m.price} <button onclick="delMenu('${m.id}')">Hapus</button></div>`).join('');
}

function addToCart(id) {
  const existing = cart.find(c => c.menuId === id);
  if (existing) existing.qty++;
  else cart.push({ menuId: id, qty: 1 });
  renderCart();
}

function renderCart() {
  $('cart').innerHTML = cart.map((c, i) => {
    const m = (window._menu || []).find(x => x.id === c.menuId);
    return `<div>${m ? m.name : c.menuId} x ${c.qty} <button onclick="cart[${i}].qty++;renderCart()">+</button> <button onclick="cart[${i}].qty=Math.max(1,cart[${i}].qty-1);renderCart()">-</button> <button onclick="cart.splice(${i},1);renderCart()">x</button></div>`;
  }).join('') || '<em>Kosong</em>';
}

async function checkout() {
  if (!cart.length) return alert('Keranjang kosong');
  try {
    const order = await api('/orders', { method: 'POST', body: JSON.stringify({ items: cart }) });
    cart = [];
    renderCart();
    alert('Order dibuat: ' + order.id + ' total Rp ' + order.total.toLocaleString('id-ID'));
    await loadOrders();
  } catch (e) { alert(e.message); }
}

async function loadOrders() {
  const data = await api('/orders');
  $('orders').innerHTML = data.orders.map(o => `
    <div class="order">
      <b>${o.id}</b> - ${o.status} - Rp ${o.total.toLocaleString('id-ID')}
      <div>${o.items.map(i => i.name + ' x' + i.qty).join(', ')}</div>
      <button onclick="setStatus('${o.id}','completed')">Selesai</button>
    </div>`).join('') || '<em>Belum ada pesanan</em>';
}

async function loadKitchen() {
  const data = await api('/orders?status=pending');
  const preparing = await api('/orders?status=preparing');
  const all = [...data.orders, ...preparing.orders];
  $('kitchenOrders').innerHTML = all.map(o => `
    <div class="order">
      <b>${o.id}</b> - ${o.status}
      <div>${o.items.map(i => i.name + ' x' + i.qty).join(', ')}</div>
      ${o.status === 'pending' ? `<button onclick="setStatus('${o.id}','preparing')">Mulai Masak</button>` : ''}
      ${o.status === 'preparing' ? `<button onclick="setStatus('${o.id}','ready')">Siap</button>` : ''}
      ${o.status === 'ready' ? `<button onclick="setStatus('${o.id}','completed')">Selesai</button>` : ''}
    </div>`).join('') || '<em>Tidak ada pesanan</em>';
}

async function setStatus(id, status) {
  await api('/orders/' + id + '/status', { method: 'PATCH', body: JSON.stringify({ status }) });
  await loadOrders();
  await loadKitchen();
}

async function delMenu(id) {
  await api('/menu/' + id, { method: 'DELETE' });
  await loadMenu();
}

async function loadAll() {
  const data = await api('/menu');
  window._menu = data.items;
  await loadMenu();
  await loadOrders();
}

// init
(async () => {
  if (token) {
    try { const me = await api('/auth/me'); $('who').textContent = me.user.username + ' (' + me.user.role + ')'; await loadAll(); }
    catch (e) { token = ''; localStorage.removeItem('token'); }
  }
})();
