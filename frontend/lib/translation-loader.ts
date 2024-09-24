import {i18n} from "@ktag/styleguide/dist/base";
import { initReactI18next } from 'react-i18next';

interface I18nProjectConfig {
    ns?: string[];
    backend?: { loadPath: string, addPath?: string };
    saveMissing?: boolean;
}
let loaded = false;
function loadTranslations() {
    if (loaded) {
        return;
    }
    const isDesign = document.body.getAttribute("ng-app") === 'flowableModeler';
    const isWork = document.querySelector('#root01')?.classList.contains('flw-engage-root');
    const isStorybook = document.body.classList.contains('sb-show-main');

    if (isDesign || isWork || isStorybook) {
        const translationApiUrl = 'https://test.ag.ch/io/translation-service/api/v1/translations/multiload?namespace={{ns}}&language={{lng}}';

        const i18nProjectConfig: I18nProjectConfig = {
            ns: ['core', 'cdev'],
        };
        if (translationApiUrl) {
            i18nProjectConfig.backend = { loadPath: translationApiUrl };
        }
        i18n.initialize(i18nProjectConfig, { plugin: initReactI18next, allowMultiLoading: true });
        loaded = true;
    }
}

export default i18n;
export {
    loadTranslations,
};