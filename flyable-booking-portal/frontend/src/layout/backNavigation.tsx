import { Box, IconButton } from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

/**
 * Simple component for rendering a back navigation button.
 * Just goes back in the history when clicked.
 */
export const BackNavigation = () => {
  const navigate = useNavigate();

  return (
    <Box pb={0.5}>
      <IconButton aria-label="back" size="large" onClick={() => navigate(-1)}>
        <ArrowBack fontSize="inherit" />
      </IconButton>
    </Box>
  );
};
