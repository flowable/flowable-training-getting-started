import React from 'react';
import { loadTranslations } from '../../lib/translation-loader';

const withTranslations = Story => {
  loadTranslations();
  return (
    <>
      <Story/>
    </>
  );
};

export default withTranslations;

export {
  withTranslations
}
