package com.flowable.training.flyable.rest.flowable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

import com.flowable.training.flyable.exception.FlowableServiceException;
import com.flowable.training.flyable.rest.flowable.dto.response.ErrorResponse;

/**
 * General exception handler for Flowable API.
 * If the exception is a FlowableServiceException, it will return a custom error response.
 */
@ControllerAdvice
public class FlowableExceptionHandler {

    @ExceptionHandler(FlowableServiceException.class)
    public ResponseEntity<ErrorResponse> handleFlowableServiceException(FlowableServiceException ex) {
        if(ex.getCause() instanceof ResourceAccessException) {
            // Probably, Flowable is down
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body( new ErrorResponse("Flowable Work is not available", "FLOWABLE_UNAVAILABLE"));
        } else {
            // Generic errors
            return ResponseEntity.status(ex.getStatusCode()).body( new ErrorResponse(ex.getMessage(), "FLOWABLE_ERROR"));
        }
    }

}
