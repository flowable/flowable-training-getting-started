import * as React from 'react';
import { Box, Card, CardActions, CardContent, FormControlLabel, Radio, RadioGroup, Typography } from '@mui/material';
import { useFlightStore } from '../../../../store/flightStore';

/**
 * Booking Wizard - Page for the Ticket Booking Wizard to choose a flight.
 */
const randomMinutes = Math.floor(Math.random() * (8 * 60 - 40 + 1)) + 40;
const generateRandomFlight = () => {
  const hours = Math.floor(randomMinutes / 60);
  const minutes = randomMinutes % 60;
  const duration = `${hours}h ${minutes}m`;

  const randomDepartureHour = Math.floor(Math.random() * 24);
  const randomDepartureMinute = Math.floor(Math.random() * 60);
  const departureTime = `${randomDepartureHour.toString().padStart(2, '0')}:${randomDepartureMinute.toString().padStart(2, '0')} ${randomDepartureHour < 12 ? 'AM' : 'PM'}`;

  const randomPrice = Math.floor(Math.random() * (800 - 200 + 1)) + 200;

  return {
    id: 1,
    departureTime,
    duration,
    airline: 'Flyable',
    price: `$${randomPrice}`,
  };
};

const ChooseFlightStep = () => {
  const { selectedFlight, setSelectedFlight } = useFlightStore();
  const [dummyFlight] = React.useState(generateRandomFlight);

  const handleFlightSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSelectedFlight(parseInt(event.target.value));
  };

  return (
    <Box>
      <Typography variant="h6">Choose Flight</Typography>
      <RadioGroup value={selectedFlight} onChange={handleFlightSelect}>
        <Card key={dummyFlight.id} variant="outlined" sx={{ mb: 2 }}>
          <CardContent>
            <Typography variant="h6">{dummyFlight.airline}</Typography>
            <Typography>Departure: {dummyFlight.departureTime}</Typography>
            <Typography>Duration: {dummyFlight.duration}</Typography>
            <Typography>Price: {dummyFlight.price}</Typography>
          </CardContent>
          <CardActions>
            <FormControlLabel value={dummyFlight.id} control={<Radio />} label="Select" />
          </CardActions>
        </Card>
      </RadioGroup>
    </Box>
  );
};

export default ChooseFlightStep;
