import { AppBar, Box, Toolbar, Typography } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import React from 'react';
import { HeaderMenu } from './headerMenu';

/**
 * Header of the Flyable Booking Portal with navigation and user information.
 */
export const Header = () => {
  return (
    <AppBar position="static" color="primary">
      <Toolbar>
        <Box display="flex" flex="auto" alignItems="center" padding={2}>
          <Link to="/" style={{ color: 'inherit', textDecoration: 'none', display: 'flex', alignItems: 'center' }}>
            <img src="img/flyable-logo.png" style={{ filter: 'brightness(0) invert(1)', height: '50px', marginRight: 5 }} alt="Logo" />
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              Flyable Portal
            </Typography>
          </Link>
        </Box>
        <Box display="flex" alignItems="center">
          <HeaderMenu />
        </Box>
      </Toolbar>
    </AppBar>
  );
};
