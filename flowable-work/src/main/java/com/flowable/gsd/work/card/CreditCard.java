package com.flowable.gsd.work.card;

public record CreditCard(
        String cardNumber,
        String cvv,
        String expiryDate
) {
}
