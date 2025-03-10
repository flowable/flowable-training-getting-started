import axios from 'axios';
import { useAuthStore } from '../store/authStore';
import { useOverlayStore } from '../store/overlayStore';

// Configuration of our axios instances. Currently, we only use flyable.
// The flowable instance could be used if we were running this frontend directly with a Flowable Work backend.
const config = {
  flowable: {
    host: process.env.FLOWABLE_WORK_API_HOST || 'localhost',
    port: process.env.FLOWABLE_WORK_API_PORT || 8090,
    protocol: process.env.FLOWABLE_WORK_API_PROTOCOL || 'http',
    basePath: process.env.FLOWABLE_WORK_API_BASE_PATH || '',
  },
  flyable: {
    host: process.env.FLYABLE_API_HOST || 'localhost',
    port: process.env.FLYABLE_API_PORT || 8093,
    protocol: process.env.FLYABLE_API_PROTOCOL || 'http',
  },
};

// Create Axios instances
export const flowableAxiosInstance = axios.create({
  baseURL: `${config.flowable.protocol}://${config.flowable.host}:${config.flowable.port}${config.flowable.basePath}`,
  withCredentials: true,
});

export const flyableAxiosInstance = axios.create({
  baseURL: `${config.flyable.protocol}://${config.flyable.host}:${config.flyable.port}`,
  withCredentials: true,
});

// Flyable Interceptors
// On request, send JWT token
flyableAxiosInstance.interceptors.request.use(
  config => {
    const { jwt } = useAuthStore.getState();
    if (jwt) {
      config.headers['Authorization'] = `Bearer ${jwt}`;
    }
    return config;
  },
  error => Promise.reject(error) // Simplified error handling
);

// On response, check for errors
flyableAxiosInstance.interceptors.response.use(
  response => response,
  error => {
    const { clearJwt, clearUserInfos } = useAuthStore.getState();
    const setFlowableUnavailable = useOverlayStore.getState().setFlowableUnavailable;

    if (error.response) {
      const { status, data } = error.response;
      const isTokenExpired = status === 401 && (data?.reason === 'EXPIRED_TOKEN' || data?.reason === 'INVALID_TOKEN');
      if (isTokenExpired) {
        clearJwt();
        clearUserInfos();
        window.location.href = '/login';
      }

      const isFlowableUnavailable = status === 503 && data?.reason === 'FLOWABLE_UNAVAILABLE';
      if (isFlowableUnavailable) {
        setFlowableUnavailable(true);
        throw new Error('Flowable is unavailable');
      }
    }
    return Promise.reject(error);
  }
);
