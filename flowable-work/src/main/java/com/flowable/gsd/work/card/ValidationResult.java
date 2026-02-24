package com.flowable.gsd.work.card;

public record ValidationResult(
        boolean isValid,
        String message
) {
}
