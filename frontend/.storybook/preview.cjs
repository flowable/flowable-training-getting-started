import {withDemolayout} from './helpers/withDemoLayout';
import {withTranslations} from './helpers/withTranslations';
import {initialize, mswLoader} from 'msw-storybook-addon';
import './preview.scss';

// Initialize MSW
initialize({
    serviceWorker: { url: '/apiMockService.js'}
});

const preview = {
    decorators: [
        withTranslations,
        withDemolayout,
    ],
    loaders: [
        mswLoader,
    ],
    parameters: {
        actions: {argTypesRegex: "^on[A-Z].*"},
        controls: {
            matchers: {
                color: /(background|color)$/i,
                date: /Date$/,
            },
        },
    }
}

export default preview;