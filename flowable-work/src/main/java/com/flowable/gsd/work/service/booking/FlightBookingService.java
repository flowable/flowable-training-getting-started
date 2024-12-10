package com.flowable.gsd.work.service.booking;

import java.time.LocalDate;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.gsd.work.rest.booking.BookFlightRequest;

@Service
public class FlightBookingService {

    private final RuntimeService runtimeService;
    private final ObjectMapper objectMapper;

    public FlightBookingService(RuntimeService runtimeService, ObjectMapper objectMapper) {
        this.runtimeService = runtimeService;
        this.objectMapper = objectMapper;
    }

    public ProcessInstance bookFlight(LocalDate departureDate, LocalDate returnDate, String departureCity, String destinationCity) {
        return runtimeService.createProcessInstanceBuilder()
                .processDefinitionKey("bookFlight")
                .variable("departureDate", departureDate)
                .variable("returnDate", returnDate)
                .variable("departureCity", departureCity)
                .variable("destinationCity", destinationCity)
                .start();
    }

    public void bookFlight(BookFlightRequest request) {
        runtimeService.createProcessInstanceBuilder()
                .variable("request", request)
                .processDefinitionKey("bookFlight")                .start();
    }
}
