import { useParams } from 'react-router-dom';
import { FlowableFlowApp } from '@flowable/work-views';
import { useQuery } from '@tanstack/react-query';
import { getAppDefinitionByKey } from '../../api/flyableApi';
import { CircularProgress } from '@mui/material';
import '@flowable/work-views/dist/index.css';

/**
 * Shows the view of a single app through the Flowable "App View" component.
 * FIXME: This is a work in progress as there are still some styling and auth issues, so it is currently not shown.
 */
export const AppView = () => {
  const { appKey } = useParams<string>();
  if (!appKey) {
    throw new Error('App key is required');
  }

  const { data: appDefinition } = useQuery({
    queryKey: ['appDefinition', appKey],
    queryFn: () => getAppDefinitionByKey(appKey),
  });

  if (!appDefinition) {
    return <CircularProgress />;
  }

  return (
    <div
      style={{
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        width: '100%',
      }}>
      <FlowableFlowApp fullScreen={false} hideNavigationElements={false} hideLogo={true} flowAppId={appKey} urlParameter="flyableWork" showLogin={true} />
    </div>
  );
};
