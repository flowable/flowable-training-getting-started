package com.flowable.training.flyable.rest.flowable.dto.response;

import java.util.Map;

/**
 * Representation of a Flowable instance (mainly process).
 * @param id The ID of the instance.
 * @param name The name of the instance.
 * @param processDefinitionId The ID of the process definition.
 * @param processVariables The variables of the instance.
 */
public record FlowableInstanceDto(
        String id,
        String name,
        String processDefinitionId,
        Map<String, Object> processVariables
) { }
