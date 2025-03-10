package com.flowable.training.flyable.service;

import com.flowable.training.flyable.exception.FlowableServiceException;
import com.flowable.training.flyable.rest.flowable.FlowableDataResponse;
import com.flowable.training.flyable.rest.flowable.dto.request.ActuatorHealth;
import com.flowable.training.flyable.rest.flowable.dto.response.FlowableAppDefinitionDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Contains methods to interact with Flowable services that require admin privileges.
 */
@Service
public class FlowableAdminService {

    private static final Log logger = LogFactory.getLog(FlowableAdminService.class);
    private final RestClient restClient;

    public FlowableAdminService(@Qualifier("adminRestClient") RestClient adminRestClient) {
        this.restClient = adminRestClient;
    }

    /**
     * Gets app definitions by the key
     *
     * @param key the key of the app
     */
    public FlowableDataResponse<FlowableAppDefinitionDto> getAppDefinitionsByKey(String key) {
        String uri = "app-api/app-repository/app-definitions?key={key}";
        try {
            FlowableDataResponse<FlowableAppDefinitionDto> response = restClient.get()
                    .uri(uri, key)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {

                    });
            assert response != null;
            return response;
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error getting app definitions: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error getting app definitions: " + ex.getMessage(), ex, 500);
        }
    }

    /**
     * Deploys a zip file as an app
     */
    public void deployApp(Resource resource) {
        String uri = "app-api/app-repository/deployments";
        try {
            // send a POST request with multi form data (file + name)
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("file", resource);
            bodyBuilder.part("creatorId", "admin");

            restClient.post()
                    .uri(uri)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(bodyBuilder.build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {

                    });
            logger.info("Deployed app: " + resource.getFilename());
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error deploying: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error deplying: " + ex.getMessage(), ex, 500);
        }
    }

    /**
     * Check health actuator endpoint of Flowable
     */
    public boolean flowableIsRunningAndHealthy() {
        try {
            ActuatorHealth health = restClient.get()
                    .uri("actuator/health")
                    .retrieve()
                    .body(ActuatorHealth.class);
            if (health == null) {
                return false;
            }
            return health.status().equals("UP");
        } catch (RestClientException ex) {
            return false;
        }
    }
}
