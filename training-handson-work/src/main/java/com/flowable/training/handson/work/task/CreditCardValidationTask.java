package com.flowable.training.handson.work.task;

import org.apache.commons.validator.routines.CreditCardValidator;
import org.flowable.common.engine.api.variable.VariableContainer;
import org.springframework.stereotype.Component;

import com.flowable.platform.tasks.AbstractPlatformTask;
import com.flowable.platform.tasks.ExtensionElementsContainer;

@Component("creditCardValidation")
public class CreditCardValidationTask extends AbstractPlatformTask {

    @Override
    public void executeTask(VariableContainer variableContainer, ExtensionElementsContainer extensionElementsContainer) {
        // Sample credit card number: 4111 1111 1111 1111
        String cardNumber = (String) variableContainer.getVariable("cardNumber");
        CreditCardValidator creditCardValidator = new CreditCardValidator();
        cardNumber = cardNumber.replaceAll("\\s*", "");
        boolean valid = creditCardValidator.isValid(cardNumber);
        variableContainer.setVariable("cardValid", valid);
    }

}
