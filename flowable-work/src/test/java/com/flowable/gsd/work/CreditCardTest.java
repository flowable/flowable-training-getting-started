package com.flowable.gsd.work;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Because executing a Spring Boot Test will actually start Flowable and therefore additional
 * infrastructure like elasticsearch is needed to run this test it is not a Unit Test but an
 * Integration Test and should therefore not be executed by maven surefire. Therefore, the ending IT
 * (for IntegrationTest) instead of Test.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(FlowableSpringExtension.class)
public class CreditCardTest {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    @Deployment(resources = "test-processes/twoUserTasks.bpmn")
    void testCreditCardValidation() {
        // First process with skipUserTask2 = false
        Map<String, Object> variables = Map.of("skipUserTask2", false);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("twoUserTasks", variables);
        assertTrue(processInstance.getProcessVariables().containsKey("skipUserTask2"));

        assertEquals(1, taskService.createTaskQuery().count());
        Task firstTask = taskService.createTaskQuery().singleResult();
        taskService.complete(firstTask.getId());

        assertEquals(1, taskService.createTaskQuery().count());
        taskService.complete(taskService.createTaskQuery().singleResult().getId());

        assertEquals(0, taskService.createTaskQuery().count());


        // Second process with skipUserTask2 = true
        variables = Map.of("skipUserTask2", true);

        processInstance = runtimeService.startProcessInstanceByKey("twoUserTasks", variables);
        assertTrue(processInstance.getProcessVariables().containsKey("skipUserTask2"));

        assertEquals(1, taskService.createTaskQuery().count());
        firstTask = taskService.createTaskQuery().singleResult();
        taskService.complete(firstTask.getId());

        assertEquals(0, taskService.createTaskQuery().count());

        // Third process with skipUserTask2 missing, should result in a crash
        runtimeService.startProcessInstanceByKey("twoUserTasks");
        assertEquals(1, taskService.createTaskQuery().count());
        firstTask = taskService.createTaskQuery().singleResult();
        Task finalFirstTask = firstTask;
        assertThrows(Exception.class, () -> taskService.complete(finalFirstTask.getId()));
    }

}
