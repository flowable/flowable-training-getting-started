import { flowableAxiosInstance as axiosInstance } from '../config/axiosConfig';
import type { FormLayout, Payload } from '@flowable/forms/flowable-forms/src/flw/Model';
import { DataResponseProcessDefinitionResponse, ProcessDefinitionResponse, ProcessInstanceResponse } from '@flowable/forms/flowable-work-api/process/processApiModel';
import { CaseInstanceResponse } from '@flowable/forms/flowable-work-api/work/model';
import { AppDefinitionRepresentation } from '@flowable/forms/flowable-work-api/work/workApiModel';
import { AxiosError } from 'axios';
////////////////////////////////////////
// NOTE:
// This API could be used instead of the flyable API if this frontend was running directly with a Flowable Work backend.
// It is currently not used anywhere in the application.
////////////////////////////////////////

// Helper function to handle API requests and errors
const apiRequest = async <T>(url: string, options?: any): Promise<T> => {
  try {
    const response = await axiosInstance(url, options); // Use axiosInstance directly
    if (response.status >= 200 && response.status < 300) {
      // Check for successful status codes
      return response.data;
    } else {
      throw new Error(`API request failed with status ${response.status}`);
    }
  } catch (error) {
    if (error instanceof AxiosError) {
      const exception = error.response?.data?.exception; // Optional chaining
      console.error(`API request failed: ${url}`, exception || error); // Include URL in error message
      throw new Error(`API request failed: ${url}`, { cause: exception || error }); // Include original error
    }
    throw error; // Re-throw if not an AxiosError
  }
};

/**
 * Fetch a form definition by its key.
 * @param instanceId - The ID of the instance (process or case).
 */
export const getWorkFormByInstanceId = async (instanceId: string): Promise<FormLayout> => {
  const scopeType = instanceId.startsWith('PRC') ? 'process' : 'case';
  return apiRequest(`/platform-api/${scopeType}-instances/${instanceId}/work-form`);
};

/**
 * Fetch (work) form data by instance ID.
 * @param instanceId - The ID of the instance (process or case).
 */
export const getWorkFormDataByInstanceId = async (instanceId: string): Promise<Payload> => {
  const scopeType = instanceId.startsWith('PRC') ? 'process' : 'case';
  return apiRequest(`/platform-api/${scopeType}-instances/${instanceId}/work-form/variables`);
};

/**
 * Fetch a form definition by its key.
 * @param onlyLatest - Whether to fetch only the latest version of the form definition.
 */
export const getProcessDefinitions = async (onlyLatest: boolean): Promise<DataResponseProcessDefinitionResponse> => apiRequest(`/process-api/repository/process-definitions?latest=${onlyLatest}`);

/**
 * Fetch a process or case definition by its key.
 * @param scopeType - The type of the scope, either 'process' or 'case'.
 * @param key - The key of the process or case definition.
 */
export const getLatestDefinitionByKey = async (scopeType: 'process' | 'case', key: string): Promise<ProcessDefinitionResponse> => {
  const url = scopeType === 'case' ? `/cmmn-api/cmmn-repository/case-definitions?key=${key}&latest=true` : `/process-api/repository/process-definitions?key=${key}&latest=true`;

  const data = await apiRequest<DataResponseProcessDefinitionResponse>(url);

  if (data.size === 0) {
    throw new Error(`No ${scopeType} definitions found with key ${key}`);
  }
  return data.data![0];
};

/**
 * Fetch a form definition by its key.
 * @param onlyLatest - Whether to fetch only the latest version of the form definition.
 */
export const getAppDefinitions = async (onlyLatest: boolean): Promise<AppDefinitionRepresentation[]> => apiRequest(`/platform-api/flow-apps`);

/**
 * Start a new instance of a process or case.
 * @param request - Request object containing the scope type, payload, and definition ID.
 */
export const startInstance = async (request: { scopeType: 'process' | 'case'; payload: Payload; definitionId: string }): Promise<ProcessInstanceResponse | CaseInstanceResponse> => {
  const { scopeType, payload, definitionId } = request;
  if (!definitionId) {
    throw new Error('Definition id is required');
  }

  const body =
    scopeType === 'process'
      ? {
          processDefinitionId: definitionId,
          values: payload,
        }
      : { caseDefinitionId: definitionId };

  return apiRequest(`/platform-api/${scopeType}-instances`, {
    method: 'post',
    data: body,
    headers: { 'Content-Type': 'application/json' },
  });
};
