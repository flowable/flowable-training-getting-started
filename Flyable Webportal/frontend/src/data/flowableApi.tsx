import axiosInstance from './axiosConfig';
import type {FormLayout, Payload} from "@flowable/forms/flowable-forms/src/flw/Model";
import {
    DataResponseProcessDefinitionResponse,
    ProcessInstanceResponse
} from "@flowable/forms/flowable-work-api/process/processApiModel";
import {CaseInstanceResponse} from "@flowable/forms/flowable-work-api/work/model";

/**
 * Fetch a form definition by its key
 * @param key - the key of the form definition
 */
export const getFormByKey = async (key: string): Promise<FormLayout> => {
    const url = `/platform-api/form-api/form-repository/form-definitions/${key}`;
    const {data, status} = await axiosInstance.get(url);
    if (status === 200) {
        return data;
    } else {
        throw new Error("Could not fetch form definition");
    }
}

/**
 * Fetch a form definition by its id
 * @param definitionId - the id of the form definition
 */
export const getStartFormByDefinitionId = async (definitionId: string | undefined): Promise<FormLayout> => {
    const url = `/platform-api/process-definitions/${definitionId}/start-form`;
    const {data, status} = await axiosInstance.get(url);
    if (status === 200) {
        return data;
    } else {
        throw new Error("Could not fetch form definition");
    }
}

/**
 * Fetch all process definitions
 * @param onlyLatest - whether to fetch only the latest version of each process definition
 */
export const getProcessDefinitions = async (onlyLatest: boolean): Promise<DataResponseProcessDefinitionResponse> => {
    const url = `/process-api/repository/process-definitions?latest=${onlyLatest}`;
    const {data, status} = await axiosInstance.get(url);
    if (status === 200) {
        return data;
    } else {
        throw new Error("Could not fetch process definitions");
    }
}

/**
 * Start a new instance of a process or case
 * @param definitionId - the id of the process or case definition
 * @param scopeType - the type of the scope, either "case" or "process"
 * @param payload - the payload to start the instance with
 */
export const startInstance = async (definitionId: string, scopeType: "case" | "process", payload: Payload): Promise<ProcessInstanceResponse | CaseInstanceResponse> => {
    const url = `/platform-api/${scopeType}-instances?${scopeType}`;
    if (scopeType === "process") {
        payload = {processDefinitionId: definitionId, ...payload};
    } else {
        payload = {caseDefinitionId: definitionId, ...payload};
    }

    try {
        const data = await axiosInstance.post(url, payload, {headers: {"Content-Type": "application/json"}});
        return data.data;
    } catch (e) {
        console.error("Could not start instance");
        return Promise.reject("Could not start instance");
    }
}