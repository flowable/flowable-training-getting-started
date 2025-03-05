import { createTheme } from '@mui/material';
import { red, indigo } from '@mui/material/colors';

const theme = createTheme({
  palette: {
    primary: {
      main: indigo[900],
    },
    secondary: {
      main: red[900],
    },
  },
});

export default theme;
