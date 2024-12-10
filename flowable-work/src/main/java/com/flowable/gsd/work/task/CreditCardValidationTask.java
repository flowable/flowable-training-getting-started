package com.flowable.gsd.work.task;

import org.flowable.common.engine.api.variable.VariableContainer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.gsd.work.service.creditcard.CreditCardService;
import com.flowable.gsd.work.service.creditcard.ValidationResult;
import com.flowable.platform.tasks.AbstractPlatformTask;
import com.flowable.platform.tasks.ExtensionElementsContainer;

/**
 * This task is responsible for validating credit cards.
 * It contains the Flowable-specific logic.
 */
@Component("creditCardValidationTask")
public class CreditCardValidationTask extends AbstractPlatformTask {

    private final CreditCardService creditCardService;
    private final ObjectMapper objectMapper;

    public CreditCardValidationTask(CreditCardService creditCardService, ObjectMapper objectMapper) {
        this.creditCardService = creditCardService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void executeTask(VariableContainer variableContainer, ExtensionElementsContainer extensionElementsContainer) {
        // Here we are reading the credit card number directly from the variable container. This will couple the model to the variable name.
        String creditCardNumber = (String) variableContainer.getVariable("creditCardNumber");
        ValidationResult validationResult = creditCardService.validateCreditCard(creditCardNumber);

        // We are setting a variable name directly, this is easy but not really recommended.
        variableContainer.setVariable("creditCardValid", true);

        // Alternatively, we can also set the whole result as a JSON
        ObjectNode validationResultJson = objectMapper.valueToTree(validationResult);
        variableContainer.setVariable("creditCardValidationResult", validationResultJson);
    }
}
