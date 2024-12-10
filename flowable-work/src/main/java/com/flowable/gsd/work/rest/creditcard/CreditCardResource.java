package com.flowable.gsd.work.rest.creditcard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowable.gsd.work.service.creditcard.CreditCardService;
import com.flowable.gsd.work.service.creditcard.ValidationResult;

/**
 * REST controller for handling credit card validation requests.
 */
@RestController
@RequestMapping("/custom-api/credit-card")
public class CreditCardResource {

    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardResource(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping("/validate/{cardNumber}")
    public ResponseEntity<ValidationResult> validateCreditCard(@PathVariable String cardNumber) {
        ValidationResult result = creditCardService.validateCreditCard(cardNumber);
        return new ResponseEntity<>(result, result.isValid() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
