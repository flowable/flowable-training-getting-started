import * as React from 'react';
import { Step, StepLabel, Stepper } from '@mui/material';

const steps = ['Flight Details', 'Choose Flight', 'Passenger Information', 'Payment', 'Review & Submit'];

/**
 * Simple wrapper for the stepper for the Ticket Booking Wizard
 */
const WizardStepper = ({ activeStep }: { activeStep: number }) => (
  <Stepper activeStep={activeStep}>
    {steps.map(label => (
      <Step key={label}>
        <StepLabel>{label}</StepLabel>
      </Step>
    ))}
  </Stepper>
);

export default WizardStepper;
