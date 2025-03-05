import { FormLayout } from '@flowable/forms/flowable-forms/src/flw/Model';

/**
 * Basic form that is displayed when no form is available.
 */
export const defaultForm: FormLayout = {
  outcomes: [
    {
      id: 'submit',
      label: 'Submit',
      value: ' SUBMIT',
    },
  ],
  rows: [
    {
      cols: [
        {
          labelAlign: 'top',
          value: 'No Form has been defined.',
          type: 'htmlComponent',
        },
      ],
    },
  ],
};
