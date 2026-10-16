const path = require('path');
const fastify = require('fastify')({ logger: true });
const cors = require('@fastify/cors');
const jwt = require('@fastify/jwt');
const staticPlugin = require('@fastify/static');

const store = require('./store');
const authRoutes = require('./routes/auth');
const menuRoutes = require('./routes/menu');
const orderRoutes = require('./routes/orders');

const JWT_SECRET = process.env.JWT_SECRET || 'pos-dev-secret-change-me';

async function build() {
  await fastify.register(cors, { origin: true });
  await fastify.register(jwt, { secret: JWT_SECRET });

  fastify.decorate('authenticate', async function (request, reply) {
    try {
      await request.jwtVerify();
    } catch (err) {
      reply.code(401).send({ error: 'unauthorized' });
    }
  });

  await fastify.register(authRoutes);
  await fastify.register(menuRoutes);
  await fastify.register(orderRoutes);

  // Serve web UI
  const webDir = path.join(__dirname, '..', '..', 'web');
  await fastify.register(staticPlugin, { root: webDir, prefix: '/' });

  fastify.get('/health', async () => ({ ok: true, service: 'pos-backend', time: new Date().toISOString() }));

  return fastify;
}

async function main() {
  const app = await build();
  const port = Number(process.env.PORT) || 3000;
  const host = process.env.HOST || '0.0.0.0';
  await app.listen({ port, host });
  console.log(`POS backend running on http://${host}:${port}`);
}

if (require.main === module) {
  main().catch(err => { console.error(err); process.exit(1); });
}

module.exports = { build };
