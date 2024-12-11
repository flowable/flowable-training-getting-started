package com.flowable.gsd.work.service.tasks;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import com.flowable.core.spring.security.SecurityUtils;
import com.flowable.gsd.work.rest.tasks.FlightTaskInformation;

@Service("flightTaskManager")
public class FlightTaskManager {

    private final TaskService taskService;

    public FlightTaskManager(TaskService taskService) {
        this.taskService = taskService;
    }

    public List<FlightTaskInformation> getFlightTaskInformation() {
        List<Task> tasks = taskService.createTaskQuery()
                .includeProcessVariables()
                .taskAssignee(SecurityUtils.getCurrentUserSecurityScope().getUserId())
                .list();

        List<FlightTaskInformation> infos = new ArrayList<>();
        for (Task task : tasks) {
            String flightNumber = (String) task.getProcessVariables().getOrDefault("flightNumber", "");
            infos.add(new FlightTaskInformation(task.getId(), task.getName(), task.getDescription(), task.getDueDate(), flightNumber));
        }
        return infos;
    }
}
