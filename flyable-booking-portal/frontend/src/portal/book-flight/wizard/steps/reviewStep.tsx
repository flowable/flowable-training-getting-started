import * as React from 'react';
import { Box, Paper, Typography } from '@mui/material';
import { useFlightStore } from '../../../../store/flightStore';

/**
 * Booking Wizard - Page for the Ticket Booking Wizard to review and confirm the entered data.
 */
const ReviewStep = () => {
  const { flightDetails, selectedFlight, passengerInfo, billingAddress } = useFlightStore();

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Review & Confirm
      </Typography>
      <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6">Flight Details</Typography>
        <Typography variant="body1">Origin: {flightDetails.origin}</Typography>
        <Typography variant="body1">Destination: {flightDetails.destination}</Typography>
        <Typography variant="body1">Departure Date: {flightDetails.departureDate?.toLocaleDateString()}</Typography>
        <Typography variant="body1">Return Date: {flightDetails.returnDate?.toLocaleDateString()}</Typography>
        <Typography variant="body1">Travel Class: {flightDetails.travelClass}</Typography>
      </Paper>
      <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6">Selected Flight</Typography>
        <Typography variant="body1">Flight ID: {selectedFlight}</Typography>
      </Paper>
      <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6">Passenger Information</Typography>
        <Typography variant="body1">
          Full Name: {passengerInfo.firstName} {passengerInfo.lastName}
        </Typography>
        <Typography variant="body1">Date of Birth: {passengerInfo.dateOfBirth?.toLocaleDateString()}</Typography>
        <Typography variant="body1">Gender: {passengerInfo.gender}</Typography>
        <Typography variant="body1">Passport Number: {passengerInfo.passportNumber}</Typography>
        <Typography variant="body1">Nationality: {passengerInfo.nationality}</Typography>
      </Paper>
      <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6">Billing Address</Typography>
        <Typography variant="body1">Address: {billingAddress.address}</Typography>
        <Typography variant="body1">City: {billingAddress.city}</Typography>
        <Typography variant="body1">State: {billingAddress.state}</Typography>
        <Typography variant="body1">ZIP Code: {billingAddress.zip}</Typography>
      </Paper>
    </Box>
  );
};

export default ReviewStep;
