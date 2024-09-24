import axios from 'axios';

// Load environment variables
const apiHost = process.env.REACT_APP_API_HOST || 'localhost';
const apiPort = process.env.REACT_APP_API_PORT || '8090';
const apiProtocol = process.env.REACT_APP_API_PROTOCOL || 'http';
const apiBasePath = process.env.REACT_APP_API_BASE_PATH || '';
const apiAuthorization = process.env.REACT_APP_API_AUTHORIZATION || '';

const instance = axios.create({
    baseURL: `${apiProtocol}://${apiHost}:${apiPort}${apiBasePath}`
});

// Set the Authorization header if available
if (apiAuthorization) {
    instance.defaults.headers.common['Authorization'] = apiAuthorization;
}

export default instance;
