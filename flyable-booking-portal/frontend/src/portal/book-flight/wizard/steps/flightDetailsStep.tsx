import * as React from 'react';
import { Box, FormControl, FormControlLabel, InputLabel, MenuItem, Radio, RadioGroup, Select, TextField, Typography } from '@mui/material';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { useFlightStore } from '../../../../store/flightStore';

/**
 * Booking Wizard - Page for the Ticket Booking Wizard to select flight details.
 * This step includes selecting the origin, destination, departure date, return date, and travel class.
 */
const FlightDetailsStep = () => {
  const { flightDetails, setFlightDetails } = useFlightStore();

  const handleInputChange = (field: string, value: any) => {
    setFlightDetails({ ...flightDetails, [field]: value });
  };

  return (
    <Box>
      <Typography variant="h6">Select Flight Details</Typography>
      <FormControl fullWidth margin="normal">
        <InputLabel>Origin</InputLabel>
        <Select label="Origin" value={flightDetails.origin} onChange={e => handleInputChange('origin', e.target.value)}>
          <MenuItem value="ZRH">Zurich</MenuItem>
          <MenuItem value="VLC">Valencia</MenuItem>
        </Select>
      </FormControl>
      <FormControl fullWidth margin="normal">
        <InputLabel>Destination</InputLabel>
        <Select label="Destination" value={flightDetails.destination} onChange={e => handleInputChange('destination', e.target.value)}>
          <MenuItem value="GVA">Geneva</MenuItem>
          <MenuItem value="MAD">Madrid</MenuItem>
        </Select>
      </FormControl>
      <DatePicker selected={flightDetails.departureDate} onChange={date => handleInputChange('departureDate', date)} customInput={<TextField label="Departure Date" fullWidth margin="normal" />} />
      <br />
      <DatePicker selected={flightDetails.returnDate} onChange={date => handleInputChange('returnDate', date)} customInput={<TextField label="Return Date (optional)" fullWidth margin="normal" />} />
      <br />
      <FormControl component="fieldset" margin="normal">
        <Typography>Travel Class</Typography>
        <RadioGroup row={true} value={flightDetails.travelClass} onChange={e => handleInputChange('travelClass', e.target.value)}>
          <FormControlLabel value="economy" control={<Radio />} label="Economy" />
          <FormControlLabel value="business" control={<Radio />} label="Business" />
          <FormControlLabel value="first" control={<Radio />} label="First Class" />
        </RadioGroup>
      </FormControl>
    </Box>
  );
};

export default FlightDetailsStep;
