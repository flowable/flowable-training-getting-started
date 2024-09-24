import {createTheme} from "@mui/material";
import { deepOrange, orange } from '@mui/material/colors';

const theme = createTheme({
    palette: {
        primary: {
            main: deepOrange[500],
        },
        secondary: {
            main: orange[500],
        },
    },
});

export default theme;