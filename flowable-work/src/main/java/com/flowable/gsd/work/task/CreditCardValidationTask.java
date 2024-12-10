package com.flowable.gsd.work.task;

import org.flowable.common.engine.api.variable.VariableContainer;
import org.flowable.engine.delegate.BpmnError;
import org.springframework.stereotype.Component;

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

    public CreditCardValidationTask(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Override
    public void executeTask(VariableContainer variableContainer, ExtensionElementsContainer extensionElementsContainer) {
        // Here we are reading the credit card number directly from the variable container. This will couple the model to the variable name.
        String creditCardNumber = (String) variableContainer.getVariable("creditCardNumber");
        ValidationResult validationResult = creditCardService.validateCreditCard(creditCardNumber);

        if(!validationResult.isValid()) {
            throw new BpmnError("creditCardInvalid", validationResult.failReason());
        }

        // We are setting a variable name directly, this is easy but not really recommended.
        variableContainer.setVariable("creditCardValid", true);
    }
}
