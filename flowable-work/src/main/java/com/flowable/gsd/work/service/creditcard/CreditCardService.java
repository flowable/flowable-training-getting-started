package com.flowable.gsd.work.service.creditcard;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for validating credit cards.
 * It contains the business logic and is independent of anything flowable-related.
 */
@Service("creditCardService")
public class CreditCardService {

    private final CreditCardValidator creditCardValidator = new CreditCardValidator();

    public ValidationResult validateCreditCard(String creditCardNumber) {
        if (StringUtils.isBlank(creditCardNumber)) {
            return new ValidationResult(false, "Credit card number is empty");
        }
        creditCardNumber = creditCardNumber.trim();
        if (!isValidCreditCard(creditCardNumber)) {
            return new ValidationResult(false, "Credit card number is invalid");
        }
        return new ValidationResult(true, "Credit card number is valid");
    }

    public boolean isValidCreditCard(String creditCardNumber) {
        creditCardNumber = creditCardNumber.replaceAll("\\s", ""); // Remove spaces
        return creditCardValidator.isValid(creditCardNumber);
    }

}
