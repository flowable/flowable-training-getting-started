package com.flowable.gsd.work.card;

import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Service("flowableCreditCardService")
public class FlowableCreditCardService {

    private final CardValidatorService cardValidatorService;
    private final ObjectMapper objectMapper;

    public FlowableCreditCardService(CardValidatorService cardValidatorService, ObjectMapper objectMapper) {
        this.cardValidatorService = cardValidatorService;
        this.objectMapper = objectMapper;
    }

    public boolean isCardValid(String cardNumber) {
        return cardValidatorService.validateCreditCard(new CreditCard(cardNumber, null, null)).isValid();
    }

    public ObjectNode validateCreditCard(String cardNumber) {
        CreditCard creditCard = new CreditCard(cardNumber, null, null);
        ValidationResult validationResult = cardValidatorService.validateCreditCard(creditCard);
        ObjectNode jsonResult = objectMapper.createObjectNode();
        jsonResult.put("isValid", validationResult.isValid());

        if (validationResult.isValid()) {
            jsonResult.put("message", "Credit card valid");
        } else {
            jsonResult.put("message", "Credit card invalid");
        }

        return jsonResult;
    }
}
