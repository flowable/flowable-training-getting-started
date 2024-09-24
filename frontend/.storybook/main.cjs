const {
  mergeConfig
} = require('vite');
const jsonImporter = require('node-sass-json-importer');
module.exports = {
  "stories": ["../lib/**/*.story.mdx", "../lib/**/*.story.@(js|jsx|ts|tsx)", "../lib/**/*.stories.mdx", "../lib/**/*.stories.@(js|jsx|ts|tsx)", "../lib/**/*.story.mdx", "../lib/**/*.story.@(js|jsx|ts|tsx)", "../lib/**/*.stories.mdx", "../lib/**/*.stories.@(js|jsx|ts|tsx)"],
  "addons": ["@storybook/addon-links", "@storybook/addon-essentials", "@storybook/addon-interactions"],
  "framework": {
    name: "@storybook/react-vite",
    options: {}
  },
  "features": {
    "storyStoreV7": true
  },
  staticDirs: ['../public'],
  async viteFinal(config, {
    configType
  }) {
    // return the customized config
    return mergeConfig(config, {
      // customize the Vite config here
      css: {
        preprocessorOptions: {
          scss: {
            includePaths: ["node_modules"],
            importer: jsonImporter()
          }
        },
        optimizeDeps: {
          include: ['storybook-dark-mode']
        }
      }
    });
  },
  docs: {
    autodocs: true
  }
};