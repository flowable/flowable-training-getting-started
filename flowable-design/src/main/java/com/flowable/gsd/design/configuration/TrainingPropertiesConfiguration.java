package com.flowable.gsd.design.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "flowable.training")
@Configuration
public class TrainingPropertiesConfiguration {

    private boolean deployDemoApps = true;

    public boolean isDeployDemoApps() {
        return deployDemoApps;
    }

    public void setDeployDemoApps(boolean deployDemoApps) {
        this.deployDemoApps = deployDemoApps;
    }
}
