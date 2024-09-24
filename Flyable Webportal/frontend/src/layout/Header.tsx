import React from 'react';
import { AppBar, Toolbar, Typography, Box } from '@mui/material';
import {Link} from "react-router-dom";

export function Header() {
    return (
        <AppBar position="static" color="primary">
            <Toolbar>
                <Box display="flex" flex="auto" alignItems="center" padding={2}>
                    <Link to="/" style={{ color: 'inherit', textDecoration: 'none', display: 'flex', alignItems: 'center' }}>
                        <img src="../flyable-logo.png" style={{filter: 'brightness(0) invert(1)',height: "50px", marginRight: 5}}  alt="Logo"/>
                        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                            Flyable Portal
                        </Typography>
                    </Link>
                </Box>
            </Toolbar>
        </AppBar>
    );
}