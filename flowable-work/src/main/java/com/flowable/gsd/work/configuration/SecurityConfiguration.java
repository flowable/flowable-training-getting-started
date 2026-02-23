package com.flowable.gsd.work.configuration;

import com.flowable.autoconfigure.frontend.FrontendProperties;
import com.flowable.autoconfigure.security.FlowableHttpSecurityCustomizer;
import com.flowable.autoconfigure.security.servlet.PlatformPathRequest;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationFailureHandler;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationSuccessHandler;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    protected ObjectProvider<FrontendProperties> frontendPropertiesProvider;

    @Bean
    @Order(10)
    public SecurityFilterChain basicDefaultSecurity(HttpSecurity http, ObjectProvider<FlowableHttpSecurityCustomizer> httpSecurityCustomizers) throws Exception {
        for (FlowableHttpSecurityCustomizer customizer : httpSecurityCustomizers.orderedStream().toList()) {
            customizer.customize(http);
        }

        http
                .logout(logout -> logout
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .logoutUrl("/auth/logout")
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // using this no op authentication entry point until https://github.com/spring-projects/spring-boot/issues/36948 gets resolved
                        .defaultAuthenticationEntryPointFor((request, response, authException) -> {
                        }, new DispatcherTypeRequestMatcher(DispatcherType.ERROR))
                        .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), AnyRequestMatcher.INSTANCE))
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/auth/login")
                        .successHandler(new AjaxAuthenticationSuccessHandler())
                        .failureHandler(new AjaxAuthenticationFailureHandler())
                )
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(PlatformPathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}