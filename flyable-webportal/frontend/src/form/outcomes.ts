import { Outcome } from '@flowable/forms/flowable-forms/src/flw/Model';

/** Default outcome for forms ("Start" button). */
export const defaultOutcome: Outcome = { id: 'submit', label: 'Start', value: 'SUBMIT', primary: true };
