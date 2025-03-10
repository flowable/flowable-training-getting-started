package com.flowable.training.flyable.security.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoResource {

    private final UserRepository userRepository;

    public UserInfoResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get current user
    @GetMapping("/flyable-api/me")
    public FlyableUserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        FlyableUserDetails user = userRepository.getUser(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return FlyableUserDto.fromUserDetails(user);
    }

}
