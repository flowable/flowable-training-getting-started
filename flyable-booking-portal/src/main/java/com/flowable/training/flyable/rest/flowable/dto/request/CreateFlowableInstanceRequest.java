package com.flowable.training.flyable.rest.flowable.dto.request;

import java.util.Map;

/**
 * Request to create a new Flowable instance (case or process).
 * @param definitionId The ID of the definition to create an instance of.
 * @param values Payload to create the instance.
 */
public record CreateFlowableInstanceRequest(
        String definitionId,
        Map<String, Object> values
) {

}
