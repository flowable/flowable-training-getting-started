package com.flowable.training.handson.work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
public class TrainingHandsOnWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingHandsOnWorkApplication.class, args);
    }
}
