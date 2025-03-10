import React from 'react';
import { getDefinitions } from '../../api/flyableApi';
import ItemList from '../../shared/itemList';
import { ProcessDefinitionResponse } from '@flowable/forms/flowable-work-api/process/processApiModel';

type DefinitionListProps = {
  scopeType: 'process' | 'case';
};

/**
 * Shows a list of definitions (cases or processes) and allows the user to start a new instance.
 */
const DefinitionList = (props: DefinitionListProps) => {
  const { scopeType } = props;

  return (
    <ItemList<ProcessDefinitionResponse>
      queryKey={['definitions', scopeType]}
      title={`Start a new ${scopeType}`}
      queryFn={() => getDefinitions(scopeType, true)}
      getDataFn={(data: any) => data['data']}
      getItemKey={item => item.id}
      getItemText={item => item.name || item.id}
      getItemSecondaryText={item => `Key: ${item.key}`}
      getItemLink={item => `/${scopeType}/${item.id}`}
      emptyMessage="No definitions found"
      errorMessage="Could not fetch definitions. Check if Flowable Work is running."
    />
  );
};

export default DefinitionList;
