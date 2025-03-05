package com.flowable.training.flyable.security.jwt.request;

/**
 * Represents a login request containing a username and password.
 *
 * @param username the username of the user
 * @param password the password of the user
 */
public record LoginRequest(
        String username,
        String password
) {

}