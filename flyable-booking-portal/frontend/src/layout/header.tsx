import { AppBar, Box, Toolbar, Typography } from '@mui/material';
import { Link } from 'react-router-dom';
import React from 'react';
import { HeaderMenu } from './headerMenu';

/**
 * Header of the Flyable Booking Portal with navigation and user information.
 */
export const Header = () => {
  return (
    <AppBar position="static" color="primary">
      <Toolbar>
        <Box sx={{ display: 'flex', alignItems: 'center', flexGrow: 1, padding: 2 }}>
          <Link to="/" style={{ color: 'inherit', textDecoration: 'none', display: 'flex', alignItems: 'center' }}>
            <img src="img/flyable-logo.png" style={{ filter: 'brightness(0) invert(1)', height: '50px', marginRight: 5 }} alt="Logo" />
            <Typography variant="h6" component="div">
              Flyable Portal
            </Typography>
          </Link>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
          <HeaderMenu />
        </Box>
      </Toolbar>
    </AppBar>
  );
};
