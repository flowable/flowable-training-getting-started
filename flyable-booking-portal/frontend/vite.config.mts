import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import { resolve } from 'node:path';
import { readFileSync, existsSync } from 'node:fs';
import tsconfigPaths from 'vite-tsconfig-paths'

export default defineConfig( (config) => {
    setEnv(config.mode);
    return {
        plugins: [
            react(),
            tsconfigPaths(),
            envPlugin(),
            devServerPlugin(),
            sourcemapPlugin(),
            buildPathPlugin(),
            basePlugin(),
            importPrefixPlugin(),
            htmlPlugin(config.mode),
            proxyPlugin(),
        ],
        define: {
            global: 'window',
        },
    };
});

function setEnv(mode: string) {
    Object.assign(
        process.env,
        loadEnv(mode, '.', ['REACT_APP_', 'NODE_ENV', 'PUBLIC_URL']),
    );
    process.env.NODE_ENV ||= mode;
    const { homepage } = JSON.parse(readFileSync('package.json', 'utf-8'));
    process.env.PUBLIC_URL ||= homepage
        ? `${
            homepage.startsWith('http') || homepage.startsWith('/')
                ? homepage
                : `/${homepage}`
        }`.replace(/\/$/, '')
        : '';
}

function envPlugin() {
    return {
        name: "env-plugin",
        config(_, { mode }) {
            const env = loadEnv(mode, ".", ["REACT_APP_", "NODE_ENV", "PUBLIC_URL"]);
            return {
                define: Object.fromEntries(
                    Object.entries(env).map(([key, value]) => [
                        `process.env.${key}`,
                        JSON.stringify(value),
                    ]),
                ),
            };
        },
    };
}

function devServerPlugin() {
    return {
        name: "dev-server-plugin",
        config(_, { mode }) {
            const { HOST, PORT, HTTPS, SSL_CRT_FILE, SSL_KEY_FILE } = loadEnv(
                mode,
                ".",
                ["HOST", "PORT", "HTTPS", "SSL_CRT_FILE", "SSL_KEY_FILE"],
            );
            const https = HTTPS === "true";
            return {
                server: {
                    host: HOST || "0.0.0.0",
                    port: parseInt(PORT || "3000", 10),
                    open: true,
                    ...(https &&
                        SSL_CRT_FILE &&
                        SSL_KEY_FILE && {
                            https: {
                                cert: readFileSync(resolve(SSL_CRT_FILE)),
                                key: readFileSync(resolve(SSL_KEY_FILE)),
                            },
                        }),
                },
            };
        },
    };
}

function sourcemapPlugin() {
    return {
        name: "sourcemap-plugin",
        config(_, { mode }) {
            const { GENERATE_SOURCEMAP } = loadEnv(mode, ".", [
                "GENERATE_SOURCEMAP",
            ]);
            return {
                build: {
                    sourcemap: GENERATE_SOURCEMAP === "true",
                },
            };
        },
    };
}

function buildPathPlugin() {
    return {
        name: "build-path-plugin",
        config(_, { mode }) {
            const { BUILD_PATH } = loadEnv(mode, ".", [
                "BUILD_PATH",
            ]);
            return {
                build: {
                    outDir: BUILD_PATH || "build",
                },
            };
        },
    };
}

function basePlugin() {
    return {
        name: "base-plugin",
        config(_, { mode }) {
            const { PUBLIC_URL } = loadEnv(mode, ".", ["PUBLIC_URL"]);
            return {
                base: PUBLIC_URL || "",
            };
        },
    };
}

function importPrefixPlugin() {
    return {
        name: "import-prefix-plugin",
        config() {
            return {
                resolve: {
                    alias: [{ find: /^~([^/])/, replacement: "$1" }],
                },
            };
        },
    };
}

function proxyPlugin() {
    return {
        name: "proxy-plugin",
        config() {
            const { proxy } = JSON.parse(readFileSync("package.json", "utf-8"));
            const publicUrl = process.env.PUBLIC_URL || "";
            const basePath = publicUrl.startsWith("http")
                ? new URL(publicUrl).pathname
                : publicUrl;
            return {
                server: {
                    proxy: {
                        "^.*": {
                            target: proxy,
                            changeOrigin: true,
                            secure: false,
                            ws: true,
                            bypass(req) {
                                const path = req.url || "";
                                const pathWithoutBase = path.replace(
                                    new RegExp(`^(${basePath})?/`),
                                    "",
                                );
                                if (req.method !== "GET") return;
                                if (
                                    !req.headers.accept?.includes("text/html") &&
                                    !existsSync(resolve("public", pathWithoutBase)) &&
                                    ![
                                        "src",
                                        "@id",
                                        "@fs",
                                        "@vite",
                                        "@react-refresh",
                                        "node_modules",
                                        "__open-in-editor",
                                    ].includes(pathWithoutBase.split("/")[0])
                                ) {
                                    return;
                                }
                                return req.url;
                            },
                        },
                    },
                },
            };
        },
    };
}

function htmlPlugin(mode: string) {
    const env = loadEnv(mode, ".", ["REACT_APP_", "NODE_ENV", "PUBLIC_URL"]);
    return {
        name: "html-plugin",
        transformIndexHtml: {
            order: "pre",
            handler(html) {
                return html.replace(/%(.*?)%/g, (match, p1) => env[p1] ?? match);
            },
        },
    };
}