package com.flowable.training.flyable.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpStatusCodeException;

import com.flowable.training.flyable.exception.FlowableServiceException;
import com.flowable.training.flyable.rest.flowable.FlowableDataResponse;
import com.flowable.training.flyable.rest.flowable.dto.response.FormDefinitionDto;
import com.flowable.training.flyable.rest.flowable.dto.response.FlowableDefinitionDto;
import com.flowable.training.flyable.rest.flowable.dto.response.FlowableInstanceDto;
import com.flowable.training.flyable.util.FlowableUtil;
import com.flowable.training.flyable.util.ScopeTypes;

/**
 * Service for managing Flowable instances.
 * It can handle both, processes and cases.
 * The scopeType can either be "process" or "case".
 */
@Service
public class FlowableInstanceService {

    private final RestClient restClient;
    private final FlowableRepositoryService flowableRepositoryService;

    /**
     * Constructs a new FlowableInstanceService with the specified RestClient and FlowableRepositoryService.
     *
     * @param restClient the RestClient to use for API calls
     * @param flowableRepositoryService the FlowableRepositoryService to use for retrieving definitions
     */
    public FlowableInstanceService(RestClient restClient, FlowableRepositoryService flowableRepositoryService) {
        this.restClient = restClient;
        this.flowableRepositoryService = flowableRepositoryService;
    }

    /**
     * Retrieves Flowable instances (case or process) by definition key.
     *
     * @param scopeType the scope type (e.g., process or case)
     * @param definitionKey the definition key
     * @return a FlowableDataResponse containing the Flowable instances
     */
    public FlowableDataResponse<FlowableInstanceDto> getInstancesByDefinitionKey(String scopeType, String definitionKey) {
        String uri = ScopeTypes.PROCESS.equals(scopeType) ? "process-api/runtime/process-instances?processDefinitionKey=" + definitionKey :
                "cmmn-api/cmmn-runtime/case-instances?caseDefinitionKey=" + definitionKey;
        try {
            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {

                    });
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Creates a Flowable instance by definition key.
     *
     * @param definitionKey the definition key
     * @param scopeType the scope type (e.g., process or case)
     * @param payload the payload to send with the request
     * @return the created FlowableInstanceDto
     */
    public FlowableInstanceDto createInstanceByKey(String definitionKey, String scopeType, Map<String, Object> payload) {
        try {
            FlowableDefinitionDto definition = flowableRepositoryService.getLatestDefinitionIdByKey(definitionKey, scopeType);
            if (definition == null) {
                throw new FlowableServiceException("Definition not found for key: " + definitionKey, null, HttpStatus.NOT_FOUND.value());
            }
            return createInstance(definition.id(), payload);
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Creates a Flowable instance by definition ID.
     *
     * @param definitionId the definition ID
     * @param payload the payload to send with the request
     * @return the created FlowableInstanceDto
     */
    public FlowableInstanceDto createInstance(String definitionId, Map<String, Object> payload) {
        String scopeType = FlowableUtil.getScopeType(definitionId);
        Map<String, Object> body = new HashMap<>(payload);
        body.put(scopeType + "DefinitionId", definitionId);
        try {
            return restClient.post()
                    .uri("/platform-api/" + scopeType + "-instances")
                    .body(body)
                    .retrieve()
                    .body(FlowableInstanceDto.class);
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error creating instance: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error creating instance: " + ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Retrieves the work form for a Flowable instance by instance ID.
     *
     * @param instanceId the instance ID
     * @return the retrieved FormDefinitionDto
     */
    public FormDefinitionDto getWorkFormByInstanceId(String instanceId) {
        String scopeType = FlowableUtil.getScopeType(instanceId);
        try {
            return restClient.get()
                    .uri("/platform-api/{scopeType}-instances/" + instanceId + "/work-form", scopeType)
                    .retrieve()
                    .body(FormDefinitionDto.class);
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Retrieves the work form data for a Flowable instance by instance ID.
     *
     * @param instanceId the instance ID
     * @return a map containing the work form data
     */
    public Map<String, Object> getWorkFormDataByInstanceId(String instanceId) {
        String scopeType = FlowableUtil.getScopeType(instanceId);
        try {
            return restClient.get()
                    .uri("/platform-api/{scopeType}-instances/" + instanceId + "/work-form/variables", scopeType)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {

                    });
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}