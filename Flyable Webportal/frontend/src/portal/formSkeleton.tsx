import React from 'react';
import {Skeleton, Stack} from "@mui/material";

function FormSkeleton() {
    return (
        <Stack spacing={1} style={{animation: 'fadeIn 0.5s ease-out'}}>
            <Skeleton variant="text" height={100} />
            <Skeleton variant="rectangular" height={60}/>
            <Skeleton variant="rounded"  height={60}/>
            <Skeleton variant="rectangular" height={60}/>
            <Skeleton variant="rounded"  height={20}/>
            <Skeleton variant="rounded"  height={60}/>
            <Skeleton variant="rounded"  height={20}/>
        </Stack>
    );
}

export default FormSkeleton;