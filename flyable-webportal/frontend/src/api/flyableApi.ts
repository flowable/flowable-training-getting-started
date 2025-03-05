import { flyableAxiosInstance as axiosInstance } from '../config/axiosConfig';
import { DataResponseProcessInstanceResponse, ProcessInstanceResponse } from '@flowable/forms/flowable-work-api/process/processApiModel';
import { AxiosResponse } from 'axios';
import { FlyableApiError } from '../error/FlyableApiError';
import { User } from './authApi';
import { Payload } from '@flowable/forms/flowable-forms/src/flw/Model';
import { AppDefinitionRepresentation } from '@flowable/forms/flowable-work-api/work/workApiModel';

type DataResponseInstanceResponse = {
  data?: ProcessInstanceResponse[];
  total?: number;
  start?: number;
  sort?: string;
  order?: string;
  size?: number;
};

/**
 * Helper function to make an API request to the Flyable backend.
 */
async function makeApiRequest<T>(method: 'get' | 'post', url: string, data?: any): Promise<T> {
  try {
    const response: AxiosResponse<T> = await axiosInstance({
      method,
      url,
      data,
      headers: data ? { 'Content-Type': 'application/json' } : undefined,
    });

    if (response.status >= 200 && response.status < 300) {
      return response.data;
    } else {
      throw new FlyableApiError(`API request to ${method.toUpperCase()} ${url} failed with status ${response.status}`); // Include status code in error
    }
  } catch (error: any) {
    const errorMessage = error.response?.data?.message || error.message || 'Unknown error';
    throw new FlyableApiError(`API request to ${method.toUpperCase()} ${url} failed: ${errorMessage}`);
  }
}

////////////////////////////////////////////////////////
// Flyable-specific ("hard coded") API functions
////////////////////////////////////////////////////////

/** Fetches all ticket processes. */
export const getTicketProcesses = async (): Promise<DataResponseProcessInstanceResponse> => makeApiRequest('get', 'flyable-api/tickets');

/** Creates a new ticket process with the given payload */
export const createTicketProcess = async (payload: any): Promise<ProcessInstanceResponse> => makeApiRequest('post', 'flyable-api/tickets', payload);

////////////////////////////////////////////////////////
// Generic API Functions
////////////////////////////////////////////////////////

/** Fetches the current work form theme. */
export const getTheme = async (): Promise<any> => makeApiRequest('get', 'flyable-api/theme');

/** Fetches all app definitions. */
export const getAppDefinitions = async (): Promise<AppDefinitionRepresentation[]> => makeApiRequest('get', 'flyable-api/app-definitions');

/** Fetches an app definition by its key. */
export const getAppDefinitionByKey = (appDefinitionKey: string): Promise<AppDefinitionRepresentation> => makeApiRequest('get', `flyable-api/app-definition/${appDefinitionKey}`);

/** Fetches information about the current user (first name, last name etc.) */
export const getCurrentUser = async (): Promise<User> => makeApiRequest('get', 'flyable-api/me');

/** Fetches all process instances. */
export const getWorkFormByInstanceId = async (instanceId: string): Promise<any> => makeApiRequest('get', `flyable-api/instances/${instanceId}/work-form`);

/** Fetches work form data by instance ID (case or process). */
export const getWorkFormDataByInstanceId = async (instanceId: string): Promise<Payload> => makeApiRequest('get', `flyable-api/instances/${instanceId}/work-form/variables`);

/** Fetches all case or process definitions. */
export const getDefinitions = async (scopeType: 'case' | 'process', onlyLatest: boolean): Promise<DataResponseProcessInstanceResponse> =>
  makeApiRequest('get', `flyable-api/${scopeType}-definitions?latest=${onlyLatest}`);

/** Fetches a start, work or task form definition by the definition ID and a scope. */
export const getFormByType = async (formType: 'work' | 'start' | 'task', scopeType: string, definitionId: string): Promise<any> =>
  makeApiRequest('get', `flyable-api/${formType}-form-definition/${scopeType}/${definitionId}`);

/** Starts a case or process instance */
export const startInstance = async (request: { payload: Payload; definitionId: string; outcome?: string }): Promise<ProcessInstanceResponse> => {
  const finalPayload = { outcome: request.outcome || 'submit', ...request.payload };
  return makeApiRequest('post', `flyable-api/instances`, {
    definitionId: request.definitionId,
    values: finalPayload,
  });
};
