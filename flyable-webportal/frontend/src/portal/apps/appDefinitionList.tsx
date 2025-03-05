import React from 'react';
import { getAppDefinitions } from '../../api/flyableApi';
import ItemList from '../../shared/itemList';
import { AppDefinitionRepresentation } from '@flowable/forms/flowable-work-api/work/workApiModel';

/**
 * Shows all the apps in Flowable Work.
 */
const AppDefinitionList = () => (
  <ItemList<AppDefinitionRepresentation>
    queryKey={['apps']}
    queryFn={getAppDefinitions}
    title="Select an app"
    getDataFn={data => data}
    getItemKey={item => item.id}
    getItemText={item => item.label}
    getItemLink={item => `/apps/${item.key}/`}
    emptyMessage="No app definitions found"
    errorMessage="Could not fetch process definitions. Check if Flowable Work is running."
  />
);

export default AppDefinitionList;
