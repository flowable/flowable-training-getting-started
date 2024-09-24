import {Form} from "@flowable/forms";
import {getStartFormByDefinitionId, startInstance} from "../data/flowableApi";
import React, {useState} from "react";
import type {Payload, FormLayout, Outcome} from "@flowable/forms/flowable-forms/src/flw/Model";
import {useNavigate, useParams} from "react-router-dom";
import {useMutation, useQuery} from "@tanstack/react-query";
import FormSkeleton from "./formSkeleton";
import {enqueueSnackbar} from "notistack";
import {defaultForm} from "./defaultForm";
import {AxiosError} from "axios";
import {Navigation} from "../layout/Navigation";

type FormWrapperProps = {
    // formKey: string;
    initialPayload?: object
    submissionUrl?: string
}


type OutcomeData = {
    payload: Payload;
    navigationUrl?: string;
    result?: any;
}

type FormErrorResponse = {
    exception: string,
    message: string
}

export function SubmittableForm(props: FormWrapperProps) {
    const {definitionId} = useParams<string>();
    const [payload, setPayload] = useState<any>(props.initialPayload || {});
    // const [formConfig, setFormConfig] = useState<FormLayout>(defaultForm);

    const navigate = useNavigate();

    const defaultOutcome: Outcome = {
        id: "submit",
        label: "Start",
        value: "SUBMIT",
        primary: true
    };

    const {data: fetchedFormDefinition, isPending, error, isSuccess} = useQuery<FormLayout, AxiosError<FormErrorResponse>>({
        queryKey: ['form', definitionId],
        queryFn: () => getStartFormByDefinitionId(definitionId),
        enabled: !!definitionId,
        retry: false,
    });

    const outcomeMutation = useMutation({
        mutationFn: (outcomeData: OutcomeData) => {
            const scopeType = definitionId?.startsWith("PRC") ? "process" : "case";
            return startInstance(definitionId ?? "", scopeType, payload)
        },
        mutationKey: ['outcome'],
        onSuccess: () => {
            enqueueSnackbar("Instance started successfully!");
            navigate("/");
        }
    });

    if (isPending) {
        return <FormSkeleton/>;
    }


    function handleChange(payload: Payload, changed?: unknown) {
        if (changed) {
            setPayload(payload);
        }
    }


    if (isSuccess) {
        return renderForm(fetchedFormDefinition);
    } else if (error && error.response?.status === 404) {
        return renderForm(defaultForm)
    }

    function renderForm(formLayout: FormLayout) {
        // Check if the form definition has outcomes, if not add the default outcomes
        if (!formLayout.outcomes || formLayout.outcomes.length === 0) {
            formLayout.outcomes = [defaultOutcome];
        }

        return (
            <>
                <Navigation/>
                <div id="formContainer" style={{animation: 'fadeIn 0.15s ease-out'}}>
                    <Form
                        additionalData={payload}
                        enabled={true}
                        config={formLayout}
                        payload={payload}
                        // onChange={(payload, changed?: unknown) => handleChange(payload, changed)}
                        onOutcomePressed={(payload, result, navigationUrl) => outcomeMutation.mutate({payload, result, navigationUrl})}
                    />
                </div>
            </>
        )
    }


    return <FormSkeleton/>;
}