const store = require('../store');

async function authRoutes(fastify) {
  fastify.post('/auth/login', async (request, reply) => {
    const { username, password } = request.body || {};
    if (!username || !password) {
      return reply.code(400).send({ error: 'username and password required' });
    }
    const user = store.users.find(u => u.username === username && u.password === password);
    if (!user) return reply.code(401).send({ error: 'invalid credentials' });
    const token = fastify.jwt.sign({ id: user.id, username: user.username, role: user.role });
    return { token, user: { id: user.id, username: user.username, role: user.role } };
  });

  fastify.get('/auth/me', { preHandler: [fastify.authenticate] }, async (request) => {
    return { user: request.user };
  });
}

module.exports = authRoutes;
