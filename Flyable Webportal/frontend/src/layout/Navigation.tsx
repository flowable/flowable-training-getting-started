import {Box, IconButton} from "@mui/material";
import React from "react";
import {ArrowBack} from "@mui/icons-material";
import {useNavigate} from "react-router-dom"; // Import useNavigate instead of useNavigation

export const Navigation = () => {
    const navigate = useNavigate(); // Use useNavigate hook

    return (
        <Box>
            <IconButton aria-label="back" size="large" onClick={() => navigate(-1)}>
                <ArrowBack fontSize="inherit"/>
            </IconButton>
        </Box>
    );
}