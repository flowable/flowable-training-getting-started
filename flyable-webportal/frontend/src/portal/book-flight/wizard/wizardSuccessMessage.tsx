import React from 'react';
import { Box, Button, Stack, Typography } from '@mui/material';

/**
 * Success message for the Ticket Booking Wizard.
 * Allows to navigate to the ticket details or the home page.
 */
const WizardSuccessMessage = ({ instanceId, navigate }: { instanceId: string; navigate: any }) => (
  <Box sx={{ textAlign: 'center', mt: 4 }}>
    <Typography variant="h4" gutterBottom>
      Success!
    </Typography>
    <Typography variant="body1" gutterBottom>
      Your ticket has been successfully created.
      <br />
      Your reference number is: <b>{instanceId}</b>
    </Typography>
    <Stack>
      <Button variant="contained" color="primary" onClick={() => navigate(`/my-flights/${instanceId}`)} sx={{ mt: 2 }}>
        Open Ticket
      </Button>
      <Button variant="contained" color="primary" onClick={() => navigate('/')} sx={{ mt: 2 }}>
        Go to Home Page
      </Button>
    </Stack>
  </Box>
);

export default WizardSuccessMessage;
