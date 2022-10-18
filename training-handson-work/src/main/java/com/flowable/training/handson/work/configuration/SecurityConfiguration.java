package com.flowable.training.handson.work.configuration;

import com.flowable.autoconfigure.frontend.FrontendProperties;
import com.flowable.autoconfigure.security.FlowableWebSecurityConfigurerAdapter;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationFailureHandler;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationSuccessHandler;
import com.flowable.platform.common.security.SecurityConstants;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

@Configuration(proxyBeanMethods = false)
@Order(10)
@EnableWebSecurity
public class SecurityConfiguration extends FlowableWebSecurityConfigurerAdapter {

    @Autowired
    protected ObjectProvider<FrontendProperties> frontendPropertiesProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .logout()
                .logoutUrl("/auth/logout");

        FrontendProperties frontendProperties = frontendPropertiesProvider.getIfAvailable();
        if (frontendProperties != null && frontendProperties.getFeatures().containsKey("formBasedLogout")
                && frontendProperties.getFeatures().get("formBasedLogout")) {
            http
                    .logout()
                    .logoutSuccessUrl("/");
        } else {
            http
                    .logout()
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        }

        http
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), AnyRequestMatcher.INSTANCE)
                .and()
                .formLogin()
                .loginProcessingUrl("/auth/login")
                .successHandler(new AjaxAuthenticationSuccessHandler())
                .failureHandler(new AjaxAuthenticationFailureHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/analytics-api/**").hasAuthority(SecurityConstants.ACCESS_REPORTS_METRICS)
                .antMatchers("/work-object-api/**").hasAuthority(SecurityConstants.ACCESS_WORKOBJECT_API)
                // allow context root for all (it triggers the loading of the initial page)
                .antMatchers("/").permitAll()
                .antMatchers(
                        "/**/*.svg", "/**/*.ico", "/**/*.png", "/**/*.woff2", "/**/*.css",
                        "/**/*.woff", "/**/*.html", "/**/*.js",
                        "/**/index.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}