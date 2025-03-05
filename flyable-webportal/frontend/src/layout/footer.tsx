import { AppBar, Toolbar, Typography } from '@mui/material';

/**
 * Footer of the application.
 */
export const Footer = () => (
  <AppBar position="static" style={{ marginTop: 'auto' }}>
    <Toolbar>
      <Typography variant="body1">© {new Date().getFullYear()} Flowable AG</Typography>
    </Toolbar>
  </AppBar>
);
