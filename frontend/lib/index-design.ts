import {loadTranslations} from './translation-loader';
import './sprite-loader';
import './index.scss';
import KtagWizard from "./components/wizard/react/KtagWizard";

loadTranslations();

if (document.body.classList.contains('theme-flowable') || document.body.classList.contains('theme-Kanton')) {
    document.documentElement.style.fontSize = 'initial';
}

export default {
    ktagWizard: KtagWizard
};
