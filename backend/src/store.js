const { randomUUID } = require('crypto');

const store = {
  users: [
    { id: 'u1', username: 'admin', password: 'admin123', role: 'admin' },
    { id: 'u2', username: 'kasir', password: 'kasir123', role: 'cashier' },
    { id: 'u3', username: 'dapur', password: 'dapur123', role: 'kitchen' }
  ],
  menu: [
    { id: 'm1', name: 'Nasi Goreng', price: 25000, category: 'Makanan', available: true },
    { id: 'm2', name: 'Mie Ayam', price: 20000, category: 'Makanan', available: true },
    { id: 'm3', name: 'Es Teh', price: 5000, category: 'Minuman', available: true },
    { id: 'm4', name: 'Kopi', price: 12000, category: 'Minuman', available: true }
  ],
  orders: [],
  transactions: [],
  nextId(prefix) { return prefix + '-' + randomUUID().slice(0, 8); },
  reset() {
    this.menu.splice(0, this.menu.length,
      { id: 'm1', name: 'Nasi Goreng', price: 25000, category: 'Makanan', available: true },
      { id: 'm2', name: 'Mie Ayam', price: 20000, category: 'Makanan', available: true },
      { id: 'm3', name: 'Es Teh', price: 5000, category: 'Minuman', available: true },
      { id: 'm4', name: 'Kopi', price: 12000, category: 'Minuman', available: true }
    );
    this.orders.splice(0, this.orders.length);
    this.transactions.splice(0, this.transactions.length);
  }
};

module.exports = store;
