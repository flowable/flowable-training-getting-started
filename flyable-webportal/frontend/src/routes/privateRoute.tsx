import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../store/authStore';

/**
 * A simple private route component that checks if the user is authenticated.
 * This is just a simple check to see if the user is authenticated by checking if the JWT is present.
 * In a real-world application, you would also want to check if the JWT is expired and refresh it if necessary.
 */
export const PrivateRoute: React.FC<{ children: React.ReactElement }> = ({ children }) => {
  const authStore = useAuthStore();
  const isAuthenticated = authStore.jwt;
  return isAuthenticated ? children : <Navigate to="/login" />;
};
