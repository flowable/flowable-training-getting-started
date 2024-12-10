package com.flowable.gsd.work.rest.booking;

import java.time.LocalDate;

import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.gsd.work.service.booking.FlightBookingService;

@RestController
public class BookFlightResource {

    private final FlightBookingService flightBookingService;

    public BookFlightResource(FlightBookingService flightBookingService, ObjectMapper objectMapper) {
        this.flightBookingService = flightBookingService;
    }

    @PostMapping("/book-flight")
    public ResponseEntity<BookFlightResponse> bookFlight(@RequestBody BookFlightRequest request) {
        LocalDate departureDate = request.getDepartureDate();
        LocalDate returnDate = request.getReturnDate();
        String departureCity = request.getDepartureCity();
        String destinationCity = request.getDestinationCity();
        ProcessInstance processInstance = flightBookingService.bookFlight(departureDate, returnDate, departureCity, destinationCity);
        // Alternatively, we could also send the whole payload as a JSON
        // flightBookingService.bookFlight(request);

        return ResponseEntity.ok(new BookFlightResponse(processInstance.getId()));
    }
}
