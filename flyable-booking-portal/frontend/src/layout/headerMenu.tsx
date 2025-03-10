import React, { useState } from 'react';
import { Avatar, Box, Menu, MenuItem, Typography } from '@mui/material';
import LogoutIcon from '@mui/icons-material/Logout';
import { useAuthStore } from '../store/authStore';
import { useNavigate } from 'react-router-dom';

/**
 * Component for displaying a user menu in the header.
 * Also contains logout logic.
 */
export const HeaderMenu = () => {
  const { userInfos, clearJwt, clearUserInfos } = useAuthStore();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const navigate = useNavigate();

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    clearJwt();
    clearUserInfos();
    handleMenuClose();
    navigate('/login');
  };

  return (
    <>
      <div onClick={handleMenuOpen} style={{ cursor: 'pointer', display: 'flex', alignItems: 'center' }}>
        <Avatar src={userInfos.avatar} alt={userInfos.username} sx={{ marginRight: 2 }} />
        <Box display="flex" flexDirection="column" alignItems="flex-start">
          <Typography variant="body1">{`${userInfos.firstName} ${userInfos.lastName}`}</Typography>
          <Typography variant="overline">{userInfos.username}</Typography>
        </Box>
      </div>
      <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleMenuClose}>
        <MenuItem onClick={handleLogout}>
          <LogoutIcon sx={{ marginRight: 1 }} />
          Logout
        </MenuItem>
      </Menu>
    </>
  );
};
