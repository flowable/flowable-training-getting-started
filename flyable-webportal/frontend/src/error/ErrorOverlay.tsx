import React from 'react';
import { useOverlayStore } from '../store/overlayStore';
import { Backdrop, CircularProgress, Typography } from '@mui/material';

/**
 * ErrorOverlay component for displaying an error message when Flowable is unavailable.
 */
const ErrorOverlay = () => {
  const { isFlowableUnavailable } = useOverlayStore();
  // @ts-expect-error VITE_FLOWABLE_APP is a valid environment variable
  const url = import.meta.env.VITE_FLOWABLE_APP || 'http://localhost:8090';

  return (
    <Backdrop open={isFlowableUnavailable} sx={theme => ({ color: '#fff', zIndex: theme.zIndex.drawer + 1 })}>
      <CircularProgress />
      <Typography variant="h6" ml={3}>
        Flowable Work appears to be unavailable.
        <br />
        URL: {url}
        <br />
        Refresh the page to try again.
      </Typography>
    </Backdrop>
  );
};

export default ErrorOverlay;
