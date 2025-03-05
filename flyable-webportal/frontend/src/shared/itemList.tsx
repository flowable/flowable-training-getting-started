import { Alert, Divider, List, ListItemText, Typography } from '@mui/material';
import ListItemButton from '@mui/material/ListItemButton';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { enqueueSnackbar } from 'notistack';
import FormSkeleton from '../form/formSkeleton';

type ItemListProps<T> = {
  queryKey: string[];
  queryFn: () => Promise<any>;
  title: string;
  getDataFn: (data: any) => T[];
  getItemKey: (item: T) => string;
  getItemText: (item: T) => string;
  getItemSecondaryText?: (item: T) => string;
  getItemLink: (item: T) => string;
  emptyMessage: string;
  errorMessage: string;
};

/**
 * Generic list component that fetches data and renders a list of items.
 */
const ItemList = <T,>(props: ItemListProps<T>) => {
  const { queryKey, queryFn, title, getItemKey, getItemText, getItemSecondaryText, getItemLink, emptyMessage, errorMessage, getDataFn } = props;

  const {
    data: allData,
    error,
    isPending,
    isError,
    isLoadingError,
  } = useQuery({
    queryKey,
    queryFn,
    retry: true,
  });

  const navigate = useNavigate();

  if (isError && error.message) enqueueSnackbar(error?.message);
  if (isLoadingError) return <Alert severity="error">{errorMessage}</Alert>;
  if (isPending) return <FormSkeleton />;
  const data = getDataFn(allData);
  if (!data || data.length === 0) return <Alert severity="info">{emptyMessage}</Alert>;

  return (
    <>
      <Typography variant="h4">{title}</Typography>
      <List component="nav" style={{ maxHeight: 500, overflowY: 'auto' }}>
        {data.map((item: any) => (
          <div key={getItemKey(item)}>
            <ListItemButton onClick={() => navigate(getItemLink(item))}>
              <ListItemText primary={getItemText(item)} secondary={getItemSecondaryText ? getItemSecondaryText(item) : undefined} />
            </ListItemButton>
            <Divider />
          </div>
        ))}
      </List>
    </>
  );
};

export default ItemList;
