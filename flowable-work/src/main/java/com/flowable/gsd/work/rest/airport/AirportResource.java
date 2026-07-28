package com.flowable.gsd.work.rest.airport;

import com.flowable.gsd.work.airport.Airport;
import com.flowable.gsd.work.airport.AirportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportResource {

    private final AirportService airportService;

    public AirportResource(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public List<Airport> getAirports(@RequestParam(required = false, defaultValue = "") String country) {
        return airportService.getAirports(country);
    }

}
