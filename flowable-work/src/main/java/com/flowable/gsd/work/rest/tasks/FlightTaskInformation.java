package com.flowable.gsd.work.rest.tasks;

import java.util.Date;

public record FlightTaskInformation(
        String taskId,
        String taskName,
        String description,
        Date dueDate,
        String flightNumber
) {

}
