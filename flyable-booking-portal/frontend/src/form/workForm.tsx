import { Form } from '@flowable/forms';
import { getWorkFormByInstanceId, getWorkFormDataByInstanceId } from '../api/flyableApi';
import type { FormLayout, Payload } from '@flowable/forms/flowable-forms/src/flw/Model';
import { useParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import FormSkeleton from './formSkeleton';

/**
 * WorkForm component for rendering a work form based on the instance ID (case or process).
 */
export const WorkForm = () => {
  const { instanceId } = useParams<string>();
  if (!instanceId) {
    throw new Error('Instance ID is missing');
  }

  // Query to fetch the form definition
  const { data: fetchedFormDefinition, isPending: isLoadingFormDefinition } = useQuery({
    queryKey: ['form', instanceId],
    queryFn: () => getWorkFormByInstanceId(instanceId),
    enabled: !!instanceId,
    refetchOnWindowFocus: false,
    retry: false,
  });

  // Query to fetch the form data (variables)
  const { data: workformData, isPending: isLoadingWorkformData } = useQuery<Payload>({
    queryKey: ['workform-data', instanceId],
    queryFn: () => getWorkFormDataByInstanceId(instanceId),
    enabled: !!instanceId,
    retry: false,
  });

  if (isLoadingFormDefinition || isLoadingWorkformData) {
    return <FormSkeleton />;
  }

  if (fetchedFormDefinition && workformData) {
    return renderForm(fetchedFormDefinition);
  }

  /**
   * Renders the form with the given layout.
   * @param {FormLayout} formLayout - Layout of the form to render.
   */
  function renderForm(formLayout: FormLayout) {
    return (
      <>
        <div id="formContainer" style={{ animation: 'fadeIn 0.15s ease-out' }}>
          <Form additionalData={workformData} enabled={false} config={formLayout} payload={workformData} />
        </div>
      </>
    );
  }

  return <FormSkeleton />;
};
