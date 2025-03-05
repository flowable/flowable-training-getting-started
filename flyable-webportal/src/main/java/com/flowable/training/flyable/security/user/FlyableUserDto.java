package com.flowable.training.flyable.security.user;

// Just a DTO for the
public record FlyableUserDto(
        String username,
        String firstName,
        String lastName,
        String avatar
) {

    public static FlyableUserDto fromUserDetails(FlyableUserDetails user) {
        return new FlyableUserDto(user.getUserId(), user.getFirstName(), user.getLastName(), user.getAvatar());
    }
}
