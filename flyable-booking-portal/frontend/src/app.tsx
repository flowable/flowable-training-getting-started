import './style/app.css';
import '@flowable/forms/flwforms.min.css';
import { Box, ThemeProvider } from '@mui/material';
import { Header } from './layout/header';
import { Footer } from './layout/footer';
import { HashRouter as Router } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { SnackbarProvider } from 'notistack';
import theme from './config/theme';
import { MainContent } from './layout/mainContent';
import FlowableFormStyles from './form/flowableStyles';
import React from 'react';
import ErrorOverlay from './error/ErrorOverlay';

/**
 * Main component for the Flyable Portal.
 */
const App = () => {
  const queryClient = new QueryClient();

  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <FlowableFormStyles />
        <SnackbarProvider />
        <ErrorOverlay />
        <Router>
          <Box className="App" display="grid" gridTemplateRows="auto 1fr auto" height="100vh">
            <Header />
            <MainContent />
            <Footer />
          </Box>
        </Router>
      </ThemeProvider>
    </QueryClientProvider>
  );
};

export default App;
