import { getTicketProcesses } from '../../api/flyableApi';
import React from 'react';
import ItemList from '../../shared/itemList';
import { TICKET_PROCESS_KEY } from '../../util/modelKeys';
import { ProcessInstanceResponse } from '@flowable/forms/flowable-work-api/process/processApiModel';

/**
 * Shows a list of all "ticket booking" process instances that the user has started.
 * Since we are using the Flowable REST API, we do not have to take care of filtering/permissions.
 */

const MyFlights = () => (
  <ItemList<ProcessInstanceResponse>
    queryKey={['processInstances', TICKET_PROCESS_KEY]}
    title="Open Process Instances"
    queryFn={getTicketProcesses}
    getDataFn={data => data?.data}
    getItemKey={item => item.id}
    getItemText={item => item.name || item.id}
    getItemLink={item => `/my-flights/${item.id}`}
    emptyMessage="No process instances found"
    errorMessage="Could not fetch process instances."
  />
);

export default MyFlights;
