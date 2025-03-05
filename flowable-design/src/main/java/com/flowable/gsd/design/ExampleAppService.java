package com.flowable.gsd.design;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.design.engine.api.idm.DesignIdentityService;
import com.flowable.design.engine.api.management.DesignManagementService;
import com.flowable.design.engine.api.runtime.ModelService;
import com.flowable.design.engine.impl.exception.FlowableConflictException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.zip.ZipInputStream;

/**
 * Deploys all apps found in the /example-apps folder
 * Only runs if the property flowable.training.deploy-demo-apps is set to true and only runs once
 */
@ConditionalOnExpression("${flowable.training.deploy-demo-apps:true}")
@Service
public class ExampleAppService implements ApplicationRunner {

    private static final Log log = LogFactory.getLog(ExampleAppService.class);
    private final ModelService modelService;
    private final DesignManagementService designManagementService;
    private final DesignIdentityService designIdentityService;


    @Value("classpath*:/example-apps/*.{zip|bar}")
    private Resource[] exampleApps;


    public ExampleAppService(ModelService modelService, ObjectMapper objectMapper, DesignManagementService designManagementService, DesignIdentityService designIdentityService) {
        this.modelService = modelService;
        this.designManagementService = designManagementService;
        this.designIdentityService = designIdentityService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (Resource resource : exampleApps) {
            try {
                ZipInputStream zipInputStream = new ZipInputStream(resource.getInputStream());
                try{
                    modelService.createModelImporter()
                            .tenantId("default")
                            .importUserId("admin")
                            .workspaceKey("default")
                            .importModels(zipInputStream);
                    log.info("Imported app: " + resource.getFilename());
                } catch(FlowableConflictException ignored) {
                    log.info("Example app already exists, skipping import: " + resource.getFilename());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}