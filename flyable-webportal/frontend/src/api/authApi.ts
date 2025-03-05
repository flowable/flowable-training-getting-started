import { FlyableApiError } from '../error/FlyableApiError';
import { flyableAxiosInstance as axiosInstance } from '../config/axiosConfig';

type AuthRequest = { username: string; password: string };
type AuthResponse = { jwt: string; user: User };
export type User = { username: string; firstName: string; lastName: string; avatar: string };

export const ANONYMOUS_USER = { username: 'anonymous', firstName: 'Anonymous', lastName: '', avatar: 'img/avatar/anonymous.png' };

/**
 * Makes API requests, handling errors and returning data.
 */
async function makeApiRequest<T>(method: 'get' | 'post', url: string, data?: any): Promise<T> {
  try {
    const response = await axiosInstance({
      method,
      url,
      data,
      headers: data ? { 'Content-Type': 'application/json' } : undefined, // Add Content-Type header if data is present
    });

    if (response.status >= 200 && response.status < 300) {
      // Check for success status codes
      return response.data;
    } else {
      throw new FlyableApiError(`API request to ${method.toUpperCase()} ${url} failed with status ${response.status}`); // Include status code in error
    }
  } catch (error: any) {
    const errorMessage = error.response?.data?.message || error.message || 'Unknown error';
    throw new FlyableApiError(`API request to ${method.toUpperCase()} ${url} failed: ${errorMessage}`); // Wrap error in custom error class
  }
}

/**
 * Logs in a user.
 * @param request - The login request (username and password).
 */
export const login = async (request: AuthRequest): Promise<AuthResponse> => {
  try {
    return await makeApiRequest('post', '/auth/login', request); // Make the API request
  } catch (error) {
    throw new FlyableApiError('Login failed'); // Handle login errors
  }
};
