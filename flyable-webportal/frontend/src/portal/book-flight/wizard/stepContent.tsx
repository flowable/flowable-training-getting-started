import * as React from 'react';
import FlightDetailsStep from './steps/flightDetailsStep';
import ChooseFlightStep from './steps/chooseFlightStep';
import PassengerInfoStep from './steps/passengerInfoStep';
import PaymentStep from './steps/paymentStep';
import ReviewStep from './steps/reviewStep';

/**
 * Wrapper for the content of the Ticket Booking Wizard.
 */
const StepContent = ({ step }: { step: number }) => {
  switch (step) {
    case 0:
      return <FlightDetailsStep />;
    case 1:
      return <ChooseFlightStep />;
    case 2:
      return <PassengerInfoStep />;
    case 3:
      return <PaymentStep />;
    case 4:
      return <ReviewStep />;
    default:
      return <React.Fragment>Unknown step</React.Fragment>;
  }
};

export default StepContent;
