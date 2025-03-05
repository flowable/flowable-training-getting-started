import * as React from 'react';
import { Box, TextField, Typography } from '@mui/material';
import { useFlightStore } from '../../../../store/flightStore';

/**
 * Booking Wizard - Page for the Ticket Booking Wizard to enter payment information.
 */
const PaymentStep = () => {
  const { billingAddress, setBillingAddress } = useFlightStore();

  const handleAddressChange = (field: string, value: any) => {
    setBillingAddress({ ...billingAddress, [field]: value });
  };

  return (
    <Box>
      <Typography variant="h6">Payment</Typography>
      <Typography variant="subtitle1">Billing Address</Typography>
      <TextField label="Address" value={billingAddress.address} onChange={e => handleAddressChange('address', e.target.value)} fullWidth margin="normal" />
      <TextField label="City" value={billingAddress.city} onChange={e => handleAddressChange('city', e.target.value)} fullWidth margin="normal" />
      <TextField label="State" value={billingAddress.state} onChange={e => handleAddressChange('state', e.target.value)} fullWidth margin="normal" />
      <TextField label="ZIP Code" value={billingAddress.zip} onChange={e => handleAddressChange('zip', e.target.value)} fullWidth margin="normal" />
      <Typography variant="body2" color="textSecondary" sx={{ mt: 2 }}>
        We currently accept payments exclusively by invoice.
      </Typography>
    </Box>
  );
};

export default PaymentStep;
