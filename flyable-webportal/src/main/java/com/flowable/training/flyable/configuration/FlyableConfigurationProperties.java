package com.flowable.training.flyable.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the Flyable application.
 */
@ConfigurationProperties(prefix = "flyable")
@Configuration
public class FlyableConfigurationProperties {

    /**
     * The URL of Flowable Work.
     */
    private String flowableUrl;

    /**
     * The secret used to sign the JWT tokens.
     */
    private String jwtSecret;

    /**
     * The seed for the faker library, used to generate users.
     */
    private int fakerSeed = 12345;

    /**
     * The technical user for Flowable Work.
     * Is only used for API calls that need a technical user (e.g. to fetch the theme).
     */
    private String flowableAdminUser = "admin";

    /**
     * The password for the technical user for Flowable Work.
     */
    private String flowableAdminPassword = "test";

    /**
     * Whether the demo apps (e.g. booking) are deployed.
     */
    private boolean deployDemoApps = true;

    public String getFlowableUrl() {
        return flowableUrl;
    }
    public void setFlowableUrl(String flowableUrl) {
        this.flowableUrl = flowableUrl;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public int getFakerSeed() {
        return fakerSeed;
    }

    public void setFakerSeed(int fakerSeed) {
        this.fakerSeed = fakerSeed;
    }

    public String getFlowableAdminUser() {
        return flowableAdminUser;
    }

    public void setFlowableAdminUser(String flowableAdminUser) {
        this.flowableAdminUser = flowableAdminUser;
    }

    public String getFlowableAdminPassword() {
        return flowableAdminPassword;
    }

    public void setFlowableAdminPassword(String flowableAdminPassword) {
        this.flowableAdminPassword = flowableAdminPassword;
    }

    public boolean isDeployDemoApps() {
        return deployDemoApps;
    }

    public void setDeployDemoApps(boolean deployDemoApps) {
        this.deployDemoApps = deployDemoApps;
    }
}
