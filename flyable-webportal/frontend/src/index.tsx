import ReactDOM from 'react-dom/client';
import './style/index.css';
import App from './app';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(<App />);

// @ts-expect-error - This is a global variable that is read by the App/Case View
global.flowable = { endpoints: { baseUrl: process.env.REACT_APP_BASE_URL || 'http://localhost:8090' } };
