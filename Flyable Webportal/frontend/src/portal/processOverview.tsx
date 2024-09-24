import React from 'react';
import {getProcessDefinitions} from '../data/flowableApi';
import {Alert, Divider, List, ListItemText, Skeleton, Typography} from '@mui/material';
import ListItemButton from '@mui/material/ListItemButton';
import {DataResponseProcessDefinitionResponse, ProcessDefinitionResponse} from "@flowable/forms/flowable-work-api/process/processApiModel";
import {useNavigate} from 'react-router-dom';
import {useQuery} from "@tanstack/react-query";
import {enqueueSnackbar} from "notistack";
import FormSkeleton from "./formSkeleton";
import {Navigation} from "../layout/Navigation";

const ProcessDefinitionsList = () => {

    const {data, error, isPending, isError, isLoadingError} = useQuery<DataResponseProcessDefinitionResponse>({
        queryKey: ['processDefinitions'],
        queryFn: () => getProcessDefinitions(true),
        retry: true
    });

    const navigate = useNavigate();

    if (isError && error.message) enqueueSnackbar(error?.message);
    if (isLoadingError) return <Alert severity="error">Could not fetch process definitions. Check if Flowable Work is running.</Alert>;
    if (isPending) return <FormSkeleton/>;

    const processDefinitions = data.data as ProcessDefinitionResponse[];

    return (
        <>
            <Navigation/>
            <Typography variant="h4">Start a new process</Typography>
            <List component="nav">
                {processDefinitions.map((processDefinition) => (
                    <>
                        <ListItemButton key={processDefinition.id} onClick={e => navigate(`/processes/${processDefinition.id}`)}>
                            <ListItemText
                                primary={processDefinition.name}
                                secondary={`Key: ${processDefinition.key}`}
                            />
                        </ListItemButton>
                        <Divider/>
                    </>
                ))}
            </List>
        </>
    );
};

export default ProcessDefinitionsList;