package com.flowable.training.flyable.rest.flowable.dto.response;

/**
 * Representation of a Flowable app definition.
 * @param id The ID of the definition.
 * @param key The key of the definition.
 * @param name The name of the definition.
 * @param deploymentId The deployment ID of the definition.
 */
public record FlowableAppDefinitionDto(
        String id,
        String key,
        String name,
        String deploymentId
) {

}
