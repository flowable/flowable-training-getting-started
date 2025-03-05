import { Form } from '@flowable/forms';
import { getFormByType, startInstance } from '../api/flyableApi';
import { useState } from 'react';
import type { FormLayout, Payload } from '@flowable/forms/flowable-forms/src/flw/Model';
import { useNavigate, useParams } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import FormSkeleton from './formSkeleton';
import { enqueueSnackbar } from 'notistack';
import { defaultForm } from './defaultForm';
import { defaultOutcome } from './outcomes';
import { FlyableApiError } from '../error/FlyableApiError';

/**
 * Props for the SubmittableForm component.
 * @property {Object} [initialPayload] - Initial payload for the form.
 * @property {'start' | 'work' | 'task'} formType - Type of the form.
 */
type FormWrapperProps = {
  initialPayload?: object;
  formType: 'start' | 'work' | 'task';
};

/**
 * SubmittableForm component for rendering and submitting forms.
 */
export const SubmittableForm = (props: FormWrapperProps) => {
  const { definitionId } = useParams<string>();
  const { formType } = props;
  const [payload, setPayload] = useState<any>(props.initialPayload || {});
  const scopeType = definitionId?.startsWith('PRC') ? 'process' : 'case';
  const queryClient = useQueryClient();

  if (!definitionId) {
    throw new Error('Definition ID is missing');
  }

  const navigate = useNavigate();

  // Query to fetch the form definition
  const {
    data: fetchedFormDefinition,
    isPending,
    error,
    isSuccess,
  } = useQuery({
    queryKey: ['form', definitionId],
    queryFn: () => getFormByType(formType, scopeType, definitionId),
    enabled: !!definitionId,
    retry: false,
  });

  // Form submission mutation
  const { mutate: submit } = useMutation({
    mutationFn: startInstance,
    mutationKey: ['outcome'],
    onError: (error: FlyableApiError) => {
      enqueueSnackbar(error.message || 'Could not start instance', { variant: 'error' });
    },
    onSuccess: () => {
      enqueueSnackbar('Instance started successfully!');
      queryClient.invalidateQueries({ queryKey: ['instances', scopeType] });
      navigate('/');
    },
  });

  // If we're still waiting for the form definition, show a loading indicator, otherwise render the form or an error
  if (isPending) {
    return <FormSkeleton />;
  } else if (isSuccess) {
    return renderForm(fetchedFormDefinition);
  } else if (error) {
    return renderForm(defaultForm);
  }

  // Renders the form with the given layout. If the form definition has no outcomes, add the default outcome
  function renderForm(formLayout: FormLayout) {
    // Check if the form definition has outcomes, if not add the default outcomes
    if (!formLayout.outcomes || formLayout.outcomes.length === 0) {
      formLayout.outcomes = [defaultOutcome];
    }

    return (
      <>
        <div id="formContainer" style={{ animation: 'fadeIn 0.15s ease-out' }}>
          <Form
            additionalData={payload}
            enabled={true}
            config={formLayout}
            payload={payload}
            onOutcomePressed={async (payload: Payload, result: any) => {
              submit({ payload, outcome: result, definitionId: definitionId || '' });
            }}
          />
        </div>
      </>
    );
  }

  return <FormSkeleton />;
};
