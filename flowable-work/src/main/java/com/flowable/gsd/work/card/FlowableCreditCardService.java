package com.flowable.gsd.work.card;

import org.springframework.stereotype.Service;

@Service("flowableCreditCardService")
public class FlowableCreditCardService {

    private final CardValidatorService cardValidatorService;

    public FlowableCreditCardService(CardValidatorService cardValidatorService) {
        this.cardValidatorService = cardValidatorService;
    }

    public boolean isCardValid(String cardNumber) {
        return cardValidatorService.validateCreditCard(new CreditCard(cardNumber, null, null)).isValid();
    }
}
