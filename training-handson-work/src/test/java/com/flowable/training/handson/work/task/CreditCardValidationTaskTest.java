package com.flowable.training.handson.work.task;

import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(FlowableSpringExtension.class)
class CreditCardValidationTaskTest {

    @Test
    @Deployment(resources = "test-processes/credit-card.bpmn20.xml")
    void executeTask_withValidCardNumber_ensureValidTrue() {

    }

}