package com.flowable.training.handson.design.configuration;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import java.util.stream.Collectors;

import jakarta.servlet.DispatcherType;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher;

import com.flowable.autoconfigure.design.security.DesignHttpSecurityCustomizer;
import com.flowable.design.security.spring.web.authentication.AjaxAuthenticationFailureHandler;
import com.flowable.design.security.spring.web.authentication.AjaxAuthenticationSuccessHandler;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    @Order(10)
    public SecurityFilterChain basicDefaultSecurity(HttpSecurity http, ObjectProvider<DesignHttpSecurityCustomizer> httpSecurityCustomizers) throws Exception {
        for (DesignHttpSecurityCustomizer customizer : httpSecurityCustomizers.orderedStream()
                .collect(Collectors.toList())) {
            customizer.customize(http);
        }

        http
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                );

        http
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // using this no op authentication entry point until https://github.com/spring-projects/spring-boot/issues/36948 gets resolved
                        .defaultAuthenticationEntryPointFor((request, response, authException) -> {}, new DispatcherTypeRequestMatcher(DispatcherType.ERROR))
                        .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), AnyRequestMatcher.INSTANCE))
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/auth/login")
                        .successHandler(new AjaxAuthenticationSuccessHandler())
                        .failureHandler(new AjaxAuthenticationFailureHandler())
                )
                .authorizeHttpRequests(configurer -> configurer
                        // allow context root for all (it triggers the loading of the initial page)
                        .requestMatchers(antMatcher("/")).permitAll()
                        .requestMatchers(
                                antMatcher("/**/*.svg"), antMatcher("/**/*.ico"), antMatcher("/**/*.png"), antMatcher("/**/*.woff2"), antMatcher("/**/*.css"),
                                antMatcher("/**/*.woff"), antMatcher("/**/*.html"), antMatcher("/**/*.js"),
                                antMatcher("/**/flowable-frontend-configuration"),
                                antMatcher("/**/index.html")).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
