import { Box, Button } from '@mui/material';
import * as React from 'react';

/**
 * Controls for the ticket booking wizard
 */
const WizardControlButtons = ({ activeStep, stepsLength, handleBack, handleNext, initializeDummyData }: any) => (
  <Box
    sx={{
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      p: 2,
    }}>
    <Button color="inherit" disabled={activeStep === 0} onClick={handleBack}>
      Back
    </Button>
    <Button onClick={handleNext}>{activeStep === stepsLength - 1 ? 'Finish' : 'Next'}</Button>
    <Button variant="contained" color="primary" onClick={initializeDummyData}>
      Initialize with Dummy Data
    </Button>
  </Box>
);

export default WizardControlButtons;
