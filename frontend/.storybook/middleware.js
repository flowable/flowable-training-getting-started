const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function expressMiddleware(router) {
    router.use(
        '/v1/onlinekarten',
        createProxyMiddleware({
            target: 'https://api.geo.ag.ch',
            changeOrigin: true,
            pathRewrite: {
                '^/v1/onlinekarten': '/v1/onlinekarten', // could be adjusted based on your needs
            },
        })
    );
};