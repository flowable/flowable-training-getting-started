package com.flowable.training.flyable.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.flowable.training.flyable.exception.FlowableServiceException;
import com.flowable.training.flyable.rest.flowable.FlowableDataResponse;
import com.flowable.training.flyable.rest.flowable.dto.response.FormDefinitionDto;
import com.flowable.training.flyable.rest.flowable.dto.response.FlowableAppDefinitionDto;
import com.flowable.training.flyable.rest.flowable.dto.response.FlowableDefinitionDto;
import com.flowable.training.flyable.util.ScopeTypes;

/**
 * Service for managing Flowable repository operations.
 */
@Service
public class FlowableRepositoryService {

    private final RestClient restClient;

    /**
     * Constructs a new FlowableRepositoryService with the specified RestClient.
     *
     * @param restClient the RestClient to use for API calls
     */
    public FlowableRepositoryService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Retrieves the latest process or case definition by key.
     *
     * @param scopeType the scope type (e.g., process or case)
     * @param definitionKey the definition key
     * @return the latest FlowableDefinitionDto
     * @throws FlowableServiceException if an error occurs while calling the Flowable API
     */
    public FlowableDefinitionDto getLatestDefinitionIdByKey(String scopeType, String definitionKey) {
        String uri = (ScopeTypes.PROCESS.equals(scopeType) ? "process-api/repository/process-definitions" : "cmmn-api/cmmn-repository/case-definitions")
                + "?key={definitionKey}&latest=true";

        try {
            FlowableDataResponse<FlowableDefinitionDto> response = restClient.get()
                    .uri(uri, definitionKey)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {

                    });
            assert response != null;
            if (response.getSize() == 0) {
                return null;
            } else {
                return response.getData().get(0);
            }
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error retrieving definition: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error retrieving definition: " + ex.getMessage(), ex, 500);
        }
    }

    /**
     * Retrieves the latest process or case definitions.
     *
     * @param scopeType the scope type (e.g., process or case)
     * @return a FlowableDataResponse containing the latest Flowable definitions
     * @throws FlowableServiceException if an error occurs while calling the Flowable API
     */
    public FlowableDataResponse<FlowableDefinitionDto> getLatestDefinitions(String scopeType) {
        String uri = ScopeTypes.PROCESS.equals(scopeType) ? "process-api/repository/process-definitions" : "cmmn-api/cmmn-repository/case-definitions";
        try {
            return restClient.get()
                    .uri(uri + "?latest=true")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {

                    });
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error retrieving definitions: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error retrieving definitions: " + ex.getMessage(), ex, 500);
        }
    }

    /**
     * Retrieves a form definition by form key.
     *
     * @param formDefinitionKey the form definition key
     * @return the retrieved FormDefinitionDto
     * @throws FlowableServiceException if an error occurs while calling the Flowable API
     */
    public FormDefinitionDto getFormDefininitionByFormKey(String formDefinitionKey) {
        try {
            return restClient.get()
                    .uri("/platform-api/form-definitions/{formDefinitionKey}", formDefinitionKey)
                    .retrieve()
                    .body(FormDefinitionDto.class);
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error retrieving form definition: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error retrieving form definition: " + ex.getMessage(), ex, 500);
        }
    }

    /**
     * Retrieves a form definition by scope type, definition ID, and form type.
     * Scope type can be either "process" or "case".
     *
     * @param scopeType the scope type (e.g., process or case)
     * @param definitionId the definition ID
     * @param formType the form type
     * @return the retrieved FormDefinitionDto
     * @throws FlowableServiceException if an error occurs while calling the Flowable API
     */
    public FormDefinitionDto getScopeFormDefinitionByType(String scopeType, String definitionId, String formType) {
        try {
            return restClient.get()
                    .uri("/platform-api/{scopeType}-definitions/{definitionId}/{formType}-form", scopeType, definitionId, formType)
                    .retrieve()
                    .body(FormDefinitionDto.class);
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error retrieving form definition: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error retrieving form definition: " + ex.getMessage(), ex, 500);
        }
    }

    /**
     * Retrieves all Flowable app definitions.
     *
     * @return a list of FlowableAppDefinitionDto
     * @throws FlowableServiceException if an error occurs while calling the Flowable API
     */
    public List<FlowableAppDefinitionDto> getAppsDefinitions() {
        try {
            return restClient.get()
                    .uri("/platform-api/flow-apps")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {

                    });
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error retrieving app definitions: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error retrieving app definitions: " + ex.getMessage(), ex, 500);
        }
    }
}