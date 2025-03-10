import { useQuery } from '@tanstack/react-query';
import { getTheme } from '../api/flyableApi';

/**
 * Style tag to make sure that the Flowable theme styles are applied when a Flowable form is loaded.
 */
const FlowableStyles = () => {
  const { data: theme, isLoading } = useQuery({
    queryKey: ['theme'],
    queryFn: getTheme,
    retry: true,
    refetchOnWindowFocus: false,
  });

  if (isLoading) {
    return null;
  }

  return <style>{theme}</style>;
};

export default FlowableStyles;
