package com.flowable.training.flyable.security.jwt.response;

/**
 * Represents a response indicating that an authentication token was denied.
 */
public record FlyableAuthTokenDeniedResponse(
        String reason
) {
    public static final String EXPIRED_TOKEN = "EXPIRED_TOKEN";
    public static final String INVALID_TOKEN = "INVALID_TOKEN";

    /**
     * Creates a new FlyableAuthTokenDeniedResponse for an expired token.
     *
     * @return a new FlyableAuthTokenDeniedResponse instance with the reason set to EXPIRED_TOKEN
     */
    public static FlyableAuthTokenDeniedResponse expiredToken() {
        return new FlyableAuthTokenDeniedResponse(EXPIRED_TOKEN);
    }

    /**
     * Creates a new FlyableAuthTokenDeniedResponse for an invalid token.
     *
     * @return a new FlyableAuthTokenDeniedResponse instance with the reason set to INVALID_TOKEN
     */
    public static FlyableAuthTokenDeniedResponse invalidToken() {
        return new FlyableAuthTokenDeniedResponse(INVALID_TOKEN);
    }
}