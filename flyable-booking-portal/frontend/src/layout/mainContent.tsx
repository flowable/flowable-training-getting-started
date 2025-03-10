import { Alert, Container, Fab, Paper } from '@mui/material';
import { ErrorBoundary, FallbackProps } from 'react-error-boundary';
import { useLocation, useNavigate } from 'react-router-dom';
import { Add } from '@mui/icons-material';
import BookingPortalRoutes from '../routes/bookingPortalRoutes';
import '../style/flwFormsStyle.css';
import { BackNavigation } from './backNavigation';

/**
 * This component renders all the meat and bones of the application.
 * It includes an error boundary handling and a floating action button for navigation.
 */
export const MainContent = () => {
  const onReset = () => {};

  const navigate = useNavigate();
  const isHome = useLocation().pathname === '/';

  const fallbackRender = (fallbackProps: FallbackProps) => (
    <Alert severity="error">
      <p>Something went wrong!</p>
      <p>{fallbackProps.error.message}</p>
    </Alert>
  );

  return (
    <Container
      component="main"
      maxWidth="xl"
      sx={{
        display: 'flex',
        flexDirection: 'column',
        flex: 1,
        overflow: 'auto',
        mt: 2,
      }}>
      <Paper elevation={2} sx={{ display: 'flex', flexDirection: 'column', flex: 1, margin: 1 }}>
        <Container sx={{ padding: 1, flex: 1 }}>
          {!isHome && <BackNavigation />}
          <ErrorBoundary fallbackRender={fallbackRender} onReset={onReset}>
            <BookingPortalRoutes />
          </ErrorBoundary>
        </Container>
      </Paper>
      <Fab aria-label="Start" color="secondary" onClick={() => navigate('/process/')} sx={{ position: 'fixed', right: 20, bottom: 100 }}>
        <Add />
      </Fab>
    </Container>
  );
};
