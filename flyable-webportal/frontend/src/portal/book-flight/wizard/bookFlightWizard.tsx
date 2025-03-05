import * as React from 'react';
import { useState } from 'react';
import { Box } from '@mui/material';
import { useFlightStore } from '../../../store/flightStore';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { enqueueSnackbar } from 'notistack';
import { createTicketProcess } from '../../../api/flyableApi';
import WizardSuccessMessage from './wizardSuccessMessage';
import WizardControlButtons from './wizardControlButtons';
import StepContent from './stepContent';
import WizardStepper from './wizardStepper';
import './style/step-styles.css';

/**
 * Main component for a simple ticket booking wizard.
 * This component serves as an example on how to gather data which then creates a process in Flowable.
 * It demonstrates how scenarios where Flowable forms may not be suitable can be handled.
 */
export const BookFlightWizard = () => {
  const [activeStep, setActiveStep] = useState(0);
  const [newProcessInstanceId, setNewProcessInstanceId] = useState('');
  const { initializeDummyData } = useFlightStore();
  const flightStore = useFlightStore.getState();
  const navigate = useNavigate();

  const steps = ['Flight Details', 'Choose Flight', 'Passenger Information', 'Payment', 'Review & Submit'];

  const { mutate: startInstanceMutation } = useMutation({
    mutationKey: ['startTicketInstance'],
    mutationFn: () => {
      const { flightDetails, passengerInfo, selectedFlight, billingAddress, optionalServices, bookingReference } = flightStore;
      const payload = {
        flightDetails,
        passengerInfo,
        selectedFlight,
        billingAddress,
        optionalServices,
        bookingReference,
      };
      return createTicketProcess(payload);
    },
    onSuccess: data => {
      setNewProcessInstanceId(data.id);
      enqueueSnackbar('Ticket created successfully!', { variant: 'success' });
    },
    onError: error => {
      enqueueSnackbar('Error creating ticket: ' + error.message, { variant: 'error' });
    },
  });

  const handleNext = () => {
    if (activeStep === steps.length - 1) {
      startInstanceMutation();
    } else {
      setActiveStep(prevActiveStep => prevActiveStep + 1);
    }
  };

  const handleBack = () => {
    setActiveStep(prevActiveStep => prevActiveStep - 1);
  };

  return (
    <>
      <Box
        sx={{
          display: 'grid',
          gridTemplateRows: '90% 10%',
          height: '74vh',
        }}>
        <Box sx={{ overflowY: 'auto' }}>
          {newProcessInstanceId ? (
            <WizardSuccessMessage instanceId={newProcessInstanceId} navigate={navigate} />
          ) : (
            <>
              <WizardStepper activeStep={activeStep} />
              <Box sx={{ mt: 2, mb: 2 }}>{activeStep !== steps.length && <StepContent step={activeStep} />}</Box>
            </>
          )}
        </Box>
        {!newProcessInstanceId && <WizardControlButtons activeStep={activeStep} stepsLength={steps.length} handleBack={handleBack} handleNext={handleNext} initializeDummyData={initializeDummyData} />}
      </Box>
    </>
  );
};
