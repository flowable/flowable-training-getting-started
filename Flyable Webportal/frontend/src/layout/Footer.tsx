import React from 'react';
import { AppBar, Toolbar, Typography } from '@mui/material';

export function Footer() {
    return (
        <AppBar position="static" style={{ marginTop: 'auto'}}>
            <Toolbar>
                <Typography variant="body1">
                    © {new Date().getFullYear()} Flowable AG
                </Typography>
            </Toolbar>
        </AppBar>
    );
}