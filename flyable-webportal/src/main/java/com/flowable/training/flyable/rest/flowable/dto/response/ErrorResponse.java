package com.flowable.training.flyable.rest.flowable.dto.response;

/**
 * Error response from Flowable API.
 * @param message
 * @param reason
 */
public record ErrorResponse(
        String message,
        String reason
) {

}
