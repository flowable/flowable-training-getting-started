package com.flowable.training.flyable.rest.flowable;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flowable.training.flyable.exception.FlowableServiceException;
import com.flowable.training.flyable.rest.flowable.dto.request.CreateFlowableInstanceRequest;
import com.flowable.training.flyable.rest.flowable.dto.response.FlowableAppDefinitionDto;
import com.flowable.training.flyable.rest.flowable.dto.response.FlowableDefinitionDto;
import com.flowable.training.flyable.rest.flowable.dto.response.FlowableInstanceDto;
import com.flowable.training.flyable.rest.flowable.dto.response.FormDefinitionDto;
import com.flowable.training.flyable.service.FlowableInstanceService;
import com.flowable.training.flyable.service.FlowableRepositoryService;
import com.flowable.training.flyable.util.ModelConstants;
import com.flowable.training.flyable.util.ScopeTypes;

/**
 * Controller to expose Flowable API endpoints.
 * For the most part, it's a wrapper around the Flowable REST API.
 */
@RestController
@RequestMapping("/flyable-api")
public class FlowableApiController {

    private final FlowableInstanceService flowableInstanceService;
    private final FlowableRepositoryService flowableRepositoryService;

    public FlowableApiController(FlowableInstanceService flowableInstanceService, FlowableRepositoryService flowableRepositoryService) {
        this.flowableInstanceService = flowableInstanceService;
        this.flowableRepositoryService = flowableRepositoryService;
    }

    @GetMapping("/app-definitions")
    public ResponseEntity<List<FlowableAppDefinitionDto>> getAppDefinitions() {
        List<FlowableAppDefinitionDto> response = flowableRepositoryService.getAppsDefinitions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/app-definition/{appDefinitionKey}")
    public ResponseEntity<FlowableAppDefinitionDto> getAppDefinitionByKey(@PathVariable String appDefinitionKey) {
        FlowableAppDefinitionDto response = flowableRepositoryService.getAppsDefinitions()
                .stream()
                .filter(appDefinition -> appDefinition.key().equals(appDefinitionKey))
                .findFirst().orElseThrow(() -> new FlowableServiceException("App definition not found", null, HttpStatus.NOT_FOUND.value()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{scopeType}-instances")
    public ResponseEntity<FlowableDataResponse<FlowableInstanceDto>> getInstances(@PathVariable String scopeType, @RequestParam String definitionKey) {
        FlowableDataResponse<FlowableInstanceDto> response = flowableInstanceService.getInstancesByDefinitionKey(scopeType, definitionKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{scopeType}-definitions")
    public ResponseEntity<FlowableDataResponse<FlowableDefinitionDto>> getGenericProcessDefinitions(@PathVariable String scopeType) {
        FlowableDataResponse<FlowableDefinitionDto> response = flowableRepositoryService.getLatestDefinitions(scopeType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/form-definition/{formDefinitionKey}")
    public ResponseEntity<FormDefinitionDto> getFormDefinitionByKey(@PathVariable String formDefinitionKey) {
        FormDefinitionDto formDefinition = flowableRepositoryService.getFormDefininitionByFormKey(formDefinitionKey);
        return ResponseEntity.ok(formDefinition);
    }

    @GetMapping("/{formType}-form-definition/{scopeType}/{definitionId}")
    public ResponseEntity<FormDefinitionDto> getFormByType(@PathVariable String formType, @PathVariable String scopeType, @PathVariable String definitionId) {
        FormDefinitionDto formDefinition = flowableRepositoryService.getScopeFormDefinitionByType(scopeType, definitionId, formType);
        return ResponseEntity.ok(formDefinition);
    }

    @GetMapping("/tickets")
    public ResponseEntity<FlowableDataResponse<FlowableInstanceDto>> getTicketProcessInstances() {
        FlowableDataResponse<FlowableInstanceDto> response = flowableInstanceService.getInstancesByDefinitionKey(ScopeTypes.PROCESS,
                ModelConstants.TICKET_PROCESS_KEY);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tickets")
    public ResponseEntity<FlowableInstanceDto> createTicketProcessInstance(@RequestBody Map<String, Object> values) {
        FlowableInstanceDto response = flowableInstanceService.createInstanceByKey(ScopeTypes.PROCESS, ModelConstants.TICKET_PROCESS_KEY, values);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/instances")
    public ResponseEntity<FlowableInstanceDto> startInstance(@RequestBody CreateFlowableInstanceRequest request) {
        FlowableInstanceDto response = flowableInstanceService.createInstance(request.definitionId(), request.values());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/instances/{instanceId}/work-form")
    public ResponseEntity<FormDefinitionDto> getWorkFormByInstanceId(@PathVariable String instanceId) {
        FormDefinitionDto formDefinition = flowableInstanceService.getWorkFormByInstanceId(instanceId);
        return ResponseEntity.ok(formDefinition);
    }

    @GetMapping("/instances/{instanceId}/work-form/variables")
    public ResponseEntity<Map<String, Object>> getWorkFormDataByInstanceId(@PathVariable String instanceId) {
        Map<String, Object> workFormData = flowableInstanceService.getWorkFormDataByInstanceId(instanceId);
        return ResponseEntity.ok(workFormData);
    }
}