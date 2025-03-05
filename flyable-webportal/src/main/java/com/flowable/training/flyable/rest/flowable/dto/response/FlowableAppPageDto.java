package com.flowable.training.flyable.rest.flowable.dto.response;

/**
 * Representation of a FlowApp page.
 * @param id The ID of the page.
 * @param key The key of the page.
 * @param label The label of the page.
 * @param order The order of the page (index).
 * @param formKey The form key associated with the page.
 * @param urlSegment The URL segment identifying the page in Flowable Work.
 */
public record FlowableAppPageDto(
        String id,
        String key,
        String label,
        int order,
        String formKey,
        String urlSegment
) {

}