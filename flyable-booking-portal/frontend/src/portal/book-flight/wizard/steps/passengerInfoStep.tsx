import * as React from 'react';
import { Box, FormControl, InputLabel, MenuItem, Select, TextField, Typography } from '@mui/material';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import countries from 'i18n-iso-countries';
import enLocale from 'i18n-iso-countries/langs/en.json';
import { useFlightStore } from '../../../../store/flightStore';

countries.registerLocale(enLocale);

const countryList = Object.entries(countries.getNames('en', { select: 'official' }));

/**
 * Booking Wizard - Page for the Ticket Booking Wizard to enter passenger information.
 */
const PassengerInfoStep = () => {
  const { passengerInfo, setPassengerInfo } = useFlightStore();

  const handleInputChange = (field: string, value: any) => {
    setPassengerInfo({ ...passengerInfo, [field]: value });
  };

  return (
    <Box>
      <Typography variant="h6">Enter Passenger Information</Typography>
      <TextField label="First Name" value={passengerInfo.firstName} onChange={e => handleInputChange('firstName', e.target.value)} fullWidth margin="normal" />
      <TextField label="Last Name" value={passengerInfo.lastName} onChange={e => handleInputChange('lastName', e.target.value)} fullWidth margin="normal" />
      <DatePicker selected={passengerInfo.dateOfBirth} onChange={date => handleInputChange('dateOfBirth', date)} customInput={<TextField label="Date of Birth" fullWidth margin="normal" />} />
      <FormControl fullWidth margin="normal">
        <InputLabel> Gender</InputLabel>
        <Select label="Gender" value={passengerInfo.gender} onChange={e => handleInputChange('gender', e.target.value)}>
          <MenuItem value="male">Male</MenuItem>
          <MenuItem value="female">Female</MenuItem>
          <MenuItem value="other">Other</MenuItem>
        </Select>
      </FormControl>
      <TextField label="Passport Number" value={passengerInfo.passportNumber} onChange={e => handleInputChange('passportNumber', e.target.value)} fullWidth margin="normal" />
      <FormControl fullWidth margin="normal">
        <InputLabel>Nationality</InputLabel>
        <Select label="Nationality" value={passengerInfo.nationality} onChange={e => handleInputChange('nationality', e.target.value)}>
          {countryList.map(([code, name]) => (
            <MenuItem key={code} value={code}>
              {name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </Box>
  );
};

export default PassengerInfoStep;
