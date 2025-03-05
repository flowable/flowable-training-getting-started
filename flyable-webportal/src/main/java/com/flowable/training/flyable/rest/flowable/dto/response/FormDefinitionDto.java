package com.flowable.training.flyable.rest.flowable.dto.response;

import java.util.List;
import java.util.Map;

/**
 * Mapped representation of a Flowable Form Definition
 * @param id The ID of the form definition.
 * @param key The key of the form definition.
 * @param name The name of the form definition.
 * @param version The version of the form definition.
 * @param metadata The metadata of the form definition.
 * @param rows The rows (actual configuration) of the form definition.
 */
public record FormDefinitionDto(
        String id,
        String key,
        String name,
        int version,
        Map<String, Object> metadata,
        List<Map<String, Object>> rows
) {

}