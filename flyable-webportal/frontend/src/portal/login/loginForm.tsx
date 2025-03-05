import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Alert, Box, Button, CircularProgress, Container, TextField, Typography } from '@mui/material';
import { useMutation } from '@tanstack/react-query';
import { login } from '../../api/authApi';
import { useAuthStore } from '../../store/authStore';

/**
 * Simple login form component.
 * Sends a request to /flyable-api/auth/login on submit.
 * It will receive a JWT token and store it in the local storage.
 */
const LoginForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const authStore = useAuthStore();

  // Clear JWT and user infos on mount
  useEffect(() => {
    authStore.clearJwt();
    authStore.clearUserInfos();
  }, []);

  const { mutate: loginMutation, isPending: isLogggingIn } = useMutation({
    mutationKey: ['login'],
    mutationFn: login,
    onSuccess: data => {
      authStore.setJwt(data.jwt);
      authStore.setUserInfos(data.user);
      navigate('/'); // Redirect to home page on successful login
    },
    onError: error => {
      setError(error.message);
    },
  });

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    loginMutation({ username, password });
  };

  if (isLogggingIn) {
    return <CircularProgress sx={{ display: 'flex', justifyContent: 'center' }} />;
  }

  return (
    <Container maxWidth="xs">
      <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Typography component="h1" variant="h5">
          Login
        </Typography>
        <Box component="form" onSubmit={handleLogin} sx={{ mt: 1 }}>
          <TextField margin="normal" required fullWidth id="username" label="Username" name="username" autoComplete="username" autoFocus value={username} onChange={e => setUsername(e.target.value)} />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
            value={password}
            onChange={e => setPassword(e.target.value)}
          />
          {error && <Alert severity="error">{error}</Alert>}
          <Button type="submit" fullWidth variant="contained" sx={{ mt: 3, mb: 2 }}>
            Login
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default LoginForm;
