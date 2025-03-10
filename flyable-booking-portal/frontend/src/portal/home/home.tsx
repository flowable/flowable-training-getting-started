import { Box, Button, Container, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

/**
 * Home screen of the Flyable Portal
 * @constructor
 */
const Home = () => {
  const navigate = useNavigate();

  return (
    <Container maxWidth="sm">
      <Box textAlign="center" marginTop={8}>
        <Typography variant="h2" component="h1" gutterBottom>
          Welcome to <span style={{ color: '#1F3245', fontWeight: 500 }}>Flya</span>
          <span style={{ color: '#a9bbc1', fontWeight: 500 }}>ble</span>
        </Typography>
        <Typography variant="h5" component="p" gutterBottom>
          Your journey starts here.
        </Typography>
        <Box style={{ display: 'flex', flexDirection: 'column', gap: '5px' }}>
          <Button variant="contained" color="primary" onClick={() => navigate('/book-flight')}>
            Book Flight
          </Button>
          <Button variant="contained" color="primary" onClick={() => navigate('/process/')}>
            Start a Process
          </Button>
          <Button variant="contained" color="primary" onClick={() => navigate('/case/')}>
            Start a Case
          </Button>

          {/*<Button variant="contained" color="primary" onClick={() => navigate('/apps/')}>*/}
          {/*  My Apps*/}
          {/*</Button>*/}
          <Button variant="contained" color="primary" onClick={() => navigate('/my-flights/')}>
            My Flights
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default Home;
