package com.flowable.gsd.work.airport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    //Zurich Airport, Zurich Switzerland, ZRH
    //Geneva Airport, Geneva, Switzerland, GVA
    //Madrid-Barajas Airport, Madrid, Spain, MAD
    private final List<Airport> airports = List.of(
            new Airport("Zurich Airport", "Zurich", "CH", "ZRH"),
            new Airport("Geneva Airport", "Geneva", "CH", "GVA"),
            new Airport("Madrid-Barajas Airport", "Madrid", "ES", "MAD")
    );

    public List<Airport> getAirports(String country) {
        String countryCleaned = country.toLowerCase().trim();
        if (StringUtils.isNotEmpty(country)) {
            return airports.stream().filter(c -> c.country().equals(countryCleaned)).toList();
        } else {
            return airports;
        }
    }

}
