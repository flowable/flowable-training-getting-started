import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { dirname, resolve } from 'path';
import { fileURLToPath } from 'node:url'
import jsonImporter from 'node-sass-json-importer';
import { nodeResolve } from '@rollup/plugin-node-resolve';
import path from 'path';

const __dirname = dirname(fileURLToPath(import.meta.url));

const modeMapping = {
    'work': {
        outDir: 'dist-work',
        entry: 'lib/index-work.ts',
        NODE_ENV: 'production',
    },
    'design': {
        outDir: 'dist-design',
        entry: 'lib/index-design.ts',
        NODE_ENV: 'production',
    },
    '*': {
        outDir: 'dist',
        entry: 'lib/index.ts',
        globals: {},
        external: [
            '@ktag/styleguide',
            'i18next',
            'i18next-http-backend',
            'react-i18next',


          'http-proxy-middleware',
          'geostyler-openlayers-parser',
          'jsts',
          '@terraformer/arcgis',
        ],
        NODE_ENV: 'production',
    },
};

// https://vitejs.dev/config/
export default defineConfig(({mode}) => {
    const modeConfig = modeMapping[mode] || modeMapping['*'];
    return ({
        define: {
            'process.env.NODE_ENV': JSON.stringify(modeConfig.NODE_ENV),
        },
        css: {
            preprocessorOptions: {
                scss: {
                    includePaths: ["node_modules"],
                    importer: jsonImporter(),
                },
            }
        },
        plugins: [
            react({ include: 'NOTE: this should not be needed in future versions (>4.0.4) https://github.com/vitejs/vite-plugin-react/pull/11#discussion_r430879201'})
        ],
        resolve: {
            alias: {
                'unfetch': path.resolve(__dirname, 'node_modules/unfetch/dist/unfetch.mjs'),
            }
        },
        build: {
            outDir: modeConfig.outDir,
            minify: false,
            lib: {
                entry: resolve(__dirname, modeConfig.entry),
                name: 'KTAG Flowable Components',
                formats: ['umd', "es"],
                // the proper extensions will be added
                fileName: (format) => `ktag-flowable-components.${format}.js`,
            },
            rollupOptions: {
                treeshake: 'recommended',
                output: {
                    name: "flowable.externals",
                    assetFileNames: (assetInfo) => {
                        if (assetInfo.name === 'style.css') return 'ktag-flowable-components.css';
                        return assetInfo.name;
                    },
                    globals: {
                        react: "flowable.React",
                        "react-dom": "flowable.ReactDOM",
                        "react-router": "flowable.ReactRouter",
                        "@flowable/forms": "flowable.Forms",
                        "@flowable/forms-work": "flowable.FormsWork",
                        "@flowable/work": "flowable.Components",
                        ...(modeConfig.globals || {}),
                    },
                },
                plugins: [
                    // nodePolyfills({include: "node_modules/@ktag/styleguide/dist/react.js"}),
                    // uglify(),
                    nodeResolve(),
                ],
                external: [
                    "react",
                    "react-dom",
                    "react-router",
                    "@flowable/forms",
                    "@flowable/work",
                    "@flowable/work-scripts",
                    "@flowable/forms-work",
                    ...(modeConfig.external || []),
                ],
            },
        }
    });
});
