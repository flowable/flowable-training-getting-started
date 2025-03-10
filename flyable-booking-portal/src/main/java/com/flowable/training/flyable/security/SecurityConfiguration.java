package com.flowable.training.flyable.security;

import com.flowable.training.flyable.security.jwt.JwtAuthenticationFilter;
import com.flowable.training.flyable.security.user.FlyableUserDetails;
import com.flowable.training.flyable.security.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration for the Flyable application.
 * This configuration is used to secure the REST API and the frontend.
 * Basic concepts: We're using JWT tokens for authentication but generate them here directly.
 * IMPORTANT: This setup is not secure and should not be used in a real application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(@Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * We are using a fake user repository and JWT token flow here.
     * In a real application, you would use a OAuth2 flow with a real users.
     * This is intended to be extended and merely serves as a starting point/template.
     *
     * @param http the HTTP security configuration
     * @return the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/auth/login",
                                "/flyable-api/theme",
                                "/assets/**",
                                "/favicon.ico",
                                "/img/**",
                                "/index.html",
                                "/*/*.css",
                                "/*/*.js",
                                "/*/*.png",
                                "/*/*.jpg",
                                "/*/*.jpeg",
                                "/*/*.gif",
                                "/*/*.svg",
                                "/*/*.woff2",
                                "/*/*.woff",
                                "/*/*.ttf",
                                "/",
                                "/*.png"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS for the application.
     * Since the frontend can run separately in port 3000, we need to allow requests from there.
     * @return the CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configures the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the authentication provider.
     * We're using an in-memory user details manager here.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(userRepository));
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Configures the user details service.
     * We're using an in-memory user details manager here.
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        Collection<UserDetails> users = new ArrayList<>();
        for (FlyableUserDetails user : userRepository.getUsers()) {
            users.add(User.withUsername(user.getUserId())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build());
        }
        return new InMemoryUserDetailsManager(users);
    }

    /**
     * Configures the password encoder (BCrypt).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}