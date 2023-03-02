package com.flowable.training.handson.work.configuration;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.logging.LogFileWebEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.flowable.actuate.autoconfigure.security.servlet.ActuatorRequestMatcher;
import com.flowable.platform.common.security.SecurityConstants;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityActuatorConfiguration {

    @Bean
    @Order(6)
    // Actuator configuration should kick in before the Form Login there should always be HTTP basic for the endpoints
    public SecurityFilterChain basicActuatorSecurity(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable();

        http
                .requestMatcher(new ActuatorRequestMatcher())
                .authorizeRequests()
                .requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class, LogFileWebEndpoint.class)).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAuthority(SecurityConstants.ACCESS_ACTUATORS)
                .anyRequest().denyAll()
                .and().httpBasic();

        return http.build();
    }
}