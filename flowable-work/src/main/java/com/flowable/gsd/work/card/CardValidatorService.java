package com.flowable.gsd.work.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("cardValidatorService")
public class CardValidatorService {

    private final ObjectMapper objectMapper;

    public CardValidatorService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ValidationResult validateCreditCard(CreditCard creditCard) {
        boolean isValid = CreditCardValidator.genericCreditCardValidator().isValid(creditCard.cardNumber());
        if (isValid) {
            return new ValidationResult(true, "Card is valid");
        }
        return new ValidationResult(false, "Card is not valid");
    }

}
