package com.flowable.gsd.work;

import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Because executing a Spring Boot Test will actually start Flowable and therefore additional
 * infrastructure like elasticsearch is needed to run this test it is not a Unit Test but an
 * Integration Test and should therefore not be executed by maven surefire. Therefore, the ending IT
 * (for IntegrationTest) instead of Test.
 */
@SpringBootTest
@ExtendWith(FlowableSpringExtension.class)
//@Deployment(resources = {"my-process.bmpn20.xml"})
class WorkApplicationIT {

    @Test
    void canStartApplication() {

    }
}