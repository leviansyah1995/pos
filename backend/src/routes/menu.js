const store = require('../store');

async function menuRoutes(fastify) {
  fastify.get('/menu', async () => ({ items: store.menu }));

  fastify.post('/menu', { preHandler: [fastify.authenticate] }, async (request, reply) => {
    const { name, price, category, available } = request.body || {};
    if (!name || price == null) return reply.code(400).send({ error: 'name and price required' });
    const item = { id: store.nextId('m'), name, price: Number(price), category: category || 'Umum', available: available !== false };
    store.menu.push(item);
    return reply.code(201).send(item);
  });

  fastify.put('/menu/:id', { preHandler: [fastify.authenticate] }, async (request, reply) => {
    const item = store.menu.find(m => m.id === request.params.id);
    if (!item) return reply.code(404).send({ error: 'not found' });
    const { name, price, category, available } = request.body || {};
    if (name != null) item.name = name;
    if (price != null) item.price = Number(price);
    if (category != null) item.category = category;
    if (available != null) item.available = !!available;
    return item;
  });

  fastify.delete('/menu/:id', { preHandler: [fastify.authenticate] }, async (request, reply) => {
    const idx = store.menu.findIndex(m => m.id === request.params.id);
    if (idx === -1) return reply.code(404).send({ error: 'not found' });
    store.menu.splice(idx, 1);
    return reply.code(204).send();
  });
}

module.exports = menuRoutes;
