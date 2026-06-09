package com.flowable.gsd.work.flight;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FlightValidationService {

    private final RuntimeService runtimeService;
    private final ObjectMapper objectMapper;

    private record FlightDetails(String origin, String destination, String departureDate, String returnDate, String travelClass) {}
    private record PassengerInfo(String firstName, String lastName, String dateOfBirth, String gender, String passportNumber, String nationality) {}
    private record BillingAddress(String address, String city, String state, String zip) {}
    private record FlightBooking(String bookingReference, FlightDetails flightDetails, String initiator, PassengerInfo passengerInfo, BillingAddress billingAddress, Integer selectedFlight) {}
    private record ValidationError(String message, String field) {}

    public FlightValidationService(RuntimeService runtimeService, ObjectMapper objectMapper) {
        this.runtimeService = runtimeService;
        this.objectMapper = objectMapper;
    }

    public ObjectNode validateFlight(String processInstanceId) {
        Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
        FlightBooking booking = objectMapper.convertValue(variables, FlightBooking.class);

        ArrayNode errors = objectMapper.createArrayNode();

        requireNonBlank(booking.bookingReference(), "Booking reference is missing", "bookingReference", errors);
        if (booking.selectedFlight() == null) addError("No flight selected", "selectedFlight", errors);

        if (booking.flightDetails() == null) {
            addError("Flight details are missing", "flightDetails", errors);
        } else {
            requireNonBlank(booking.flightDetails().origin(), "Origin is missing", "flightDetails.origin", errors);
            requireNonBlank(booking.flightDetails().destination(), "Destination is missing", "flightDetails.destination", errors);
            requireNonBlank(booking.flightDetails().departureDate(), "Departure date is missing", "flightDetails.departureDate", errors);
        }

        if (booking.passengerInfo() == null) {
            addError("Passenger information is missing", "passengerInfo", errors);
        } else {
            requireNonBlank(booking.passengerInfo().firstName(), "First name is missing", "passengerInfo.firstName", errors);
            requireNonBlank(booking.passengerInfo().lastName(), "Last name is missing", "passengerInfo.lastName", errors);
            requireNonBlank(booking.passengerInfo().passportNumber(), "Passport number is missing", "passengerInfo.passportNumber", errors);
        }

        ObjectNode result = objectMapper.createObjectNode();
        result.put("valid", errors.isEmpty());
        if (!errors.isEmpty()) {
            result.set("errors", errors);
        }
        return result;
    }

    private static void requireNonBlank(String value, String message, String field, ArrayNode errors) {
        if (StringUtils.isBlank(value)) addError(message, field, errors);
    }

    private static void addError(String message, String field, ArrayNode errors) {
        errors.addObject().put("message", message).put("field", field);
    }

}
