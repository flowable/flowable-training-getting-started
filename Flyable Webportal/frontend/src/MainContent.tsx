import {Alert, Container, Fab, Grid, Paper} from "@mui/material";
import {ErrorBoundary, FallbackProps} from "react-error-boundary";
import {Route, Routes, useNavigate} from "react-router-dom";
import ProcessDefinitionsList from "./portal/processOverview";
import {SubmittableForm} from "./portal/submittableForm";
import React from "react";
import {Add} from "@mui/icons-material";
import Home from "./portal/home";

export const MainContent = () => {

    const onReset = () => {    };
    const navigate = useNavigate();

    const fallbackRender = (fallbackProps: FallbackProps) => (
        <Alert severity="error">
            <p>Something went wrong!</p>
            <p>{fallbackProps.error}</p>
        </Alert>
    );

    return <Container component="main" maxWidth="xl">
        <Grid container spacing={3} justifyContent="center" alignItems="center" style={{minHeight: "80vh"}}>
            <Grid item xs={12} sm={8} md={6}>
                <Paper elevation={3} style={{minHeight: "70vh"}}>
                    <Container style={{padding: 20}}>
                        <ErrorBoundary fallbackRender={p => fallbackRender(p)} onReset={onReset}>
                            <Routes>
                                <Route path="/" element={<Home/>}/>
                                <Route path="processes" element={<ProcessDefinitionsList/>}/>
                                <Route path="processes?/:definitionId" element={<SubmittableForm/>}/>
                            </Routes>
                        </ErrorBoundary>
                    </Container>
                </Paper>
            </Grid>
        </Grid>
        <Fab aria-label="Start" color="secondary" onClick={() => navigate('/processes')} style={{position: 'fixed', right: 20, bottom: 100}}>
            <Add/>
        </Fab>
    </Container>;
};