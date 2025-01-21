package com.flowable.gsd.work.configuration;

import com.flowable.autoconfigure.frontend.FrontendProperties;
import com.flowable.autoconfigure.security.FlowableHttpSecurityCustomizer;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationFailureHandler;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationSuccessHandler;
import com.flowable.platform.common.security.SecurityConstants;
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

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

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
                .logout(logout -> logout.logoutUrl("/auth/logout"));

        FrontendProperties frontendProperties = frontendPropertiesProvider.getIfAvailable();
        if (frontendProperties != null && frontendProperties.getFeatures().containsKey("formBasedLogout")
                && frontendProperties.getFeatures().get("formBasedLogout")) {
            http
                    .logout(logout -> logout.logoutSuccessUrl("/"));
        } else {
            http
                    .logout(logout -> logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()));
        }

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
                        .requestMatchers(antMatcher("/analytics-api/**")).hasAuthority(SecurityConstants.ACCESS_REPORTS_METRICS)
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