package com.flowable.training.handson.work.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(FlowableSpringExtension.class)
class CreditCardValidationTaskTest {

    private static final String VARIABLE_CARD_NUMBER = "cardNumber";
    private static final String VARIABLE_IS_VALID_CARD = "isValidCard";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Test
    @Deployment(resources = "processes/credit-card.bpmn20.xml")
    void executeTask_withValidCardNumber_ensureValidTrue() {
        // Create a new process instance
        ProcessInstance instance = this.runtimeService.createProcessInstanceBuilder()
                .processDefinitionKey("exercise3") // refers to the ID in credit-card.bpmn20.xml line 3: <process id="exercise3" ...>
                .variable(VARIABLE_CARD_NUMBER, "4111 1111 1111 1111") // normally provided through the start form, used in line 20 of the BPMN
                .start();

        assertThat(instance).isNotNull();

        // Search for the process instance in the runtime service (will only work in case the instance is still running)
        ProcessInstance processInstanceByQuery = this.runtimeService.createProcessInstanceQuery()
                .processInstanceId(instance.getId())
                // .includeProcessVariables() -> would be required to retrieve all variables
                .singleResult();
        assertThat(processInstanceByQuery).isNotNull();
        assertThat(processInstanceByQuery.getProcessVariables()).isEqualTo(Collections.emptyMap());

        // Search for just a variable inside the instance
        VariableInstance isValidCard = this.runtimeService.createVariableInstanceQuery()
                .processInstanceId(instance.getId())
                .variableName(VARIABLE_IS_VALID_CARD) // defined in line 22 in the BPMN
                .singleResult();
        assertThat(isValidCard)
                .isNotNull()
                .extracting(VariableInstance::getValue)
                .isEqualTo(true);

        // The BPMN model has a user task after the service, this is the reason why the last few queries worked with the
        // runtime service. In case the process instance is ended, we would need to use the history service (see below).
        // The next few statements will complete the user task

        // Search for a task
        List<Task> tasks = this.taskService.createTaskQuery()
                .processInstanceId(instance.getId())
                .list(); // There is only one task, but this shows how a query with a list could be created
        for (Task task : tasks) {
            // Complete a task by a given ID
            this.taskService.complete(task.getId());
        }

        // Same as above, only that this time we don't expect a result. Obviously, this would normally not be part of your test

        // Search for the process instance in the runtime service (will only work in case the instance is still running)
        ProcessInstance processInstanceByQueryAfterComplete = this.runtimeService.createProcessInstanceQuery()
                .processInstanceId(instance.getId())
                // .includeProcessVariables() -> would be required to retrieve all variables
                .singleResult();
        assertThat(processInstanceByQueryAfterComplete).isNull();

        // Search for the process instances in the history service
        HistoricTaskInstance historicTaskInstance = this.historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(instance.getId())
                .includeProcessVariables() // -> let's retrieve the variables directly to show the difference. A lot of variables might impact the performace here.
                .singleResult();
        assertThat(historicTaskInstance).isNotNull();
        Map<String, Object> processVariables = historicTaskInstance.getProcessVariables();
        assertThat(processVariables).isNotNull();
        assertThat(processVariables.get(VARIABLE_IS_VALID_CARD)).isEqualTo(true);

        // Cleanup will be done automatically by the @Deployment annotation
    }

    // potentially more test cases to also test invalid credit card etc.

}
