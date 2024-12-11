package com.flowable.gsd.work.rest.tasks;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowable.gsd.work.service.tasks.FlightTaskManager;

@RestController
@RequestMapping("/airline-api")
public class MyTasksResource {

    private final FlightTaskManager flightTaskManager;

    public MyTasksResource(FlightTaskManager flightTaskManager) {
        this.flightTaskManager = flightTaskManager;
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<FlightTaskInformation>> getMyTasks() {
        return ResponseEntity.ok(flightTaskManager.getFlightTaskInformation());
    }

}
