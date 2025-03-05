package com.flowable.training.flyable.security.jwt.response;

import com.flowable.training.flyable.security.user.FlyableUserDetails;
import com.flowable.training.flyable.security.user.FlyableUserDto;

/**
 * Represents the authentication response containing a JWT token and user details.
 */
public record FlyableAuthResponse(
        String jwt,
        FlyableUserDto user
) {

    /**
     * Creates a new FlyableAuthResponse from the given JWT token and user details.
     *
     * @param jwt  the JWT token
     * @param user the user details
     * @return a new FlyableAuthResponse instance
     */
    public static FlyableAuthResponse fromUser(String jwt, FlyableUserDetails user) {
        return new FlyableAuthResponse(jwt,
                FlyableUserDto.fromUserDetails(user));
    }

}