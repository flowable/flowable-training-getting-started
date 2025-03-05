package com.flowable.training.flyable.security.jwt;

import com.flowable.training.flyable.security.jwt.request.LoginRequest;
import com.flowable.training.flyable.security.jwt.response.FlyableAuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flowable.training.flyable.security.user.FlyableUserDetails;
import com.flowable.training.flyable.security.user.UserRepository;

/**
 * REST controller for handling JWT authentication.
 */
@RestController
public class JwtAuthController {

    private final JwtTokenUtil jwtUtils;
    private final UserRepository userRepository;

    /**
     * Constructs a new JwtAuthController with the specified JWT utility and user repository.
     *
     * @param jwtUtils the JWT utility
     * @param userRepository the user repository
     */
    public JwtAuthController(JwtTokenUtil jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginRequest the login request containing the username and password
     * @return a ResponseEntity containing the authentication response with the JWT token and user details
     */
    @PostMapping("/auth/login")
    public ResponseEntity<FlyableAuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        FlyableUserDetails user = userRepository.getUser(loginRequest.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwt = jwtUtils.generateJwtToken(loginRequest.username());

        return ResponseEntity.ok(FlyableAuthResponse.fromUser(jwt, user));
    }
}