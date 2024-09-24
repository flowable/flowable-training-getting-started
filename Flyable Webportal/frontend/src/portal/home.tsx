import React from 'react';
import { Typography, Container, Box, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';

export default () => {
    const navigate = useNavigate();

    return (
        <Container maxWidth="sm">
            <Box textAlign="center" marginTop={8}>
                <Typography variant="h2" component="h1" gutterBottom>
                    Welcome to Flyable
                </Typography>
                <Typography variant="h5" component="p" gutterBottom>
                    Your journey starts here.
                </Typography>
                <Box marginTop={4}>
                    <Button variant="contained" color="primary" onClick={() => navigate('/processes')}>
                        Get Started
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};