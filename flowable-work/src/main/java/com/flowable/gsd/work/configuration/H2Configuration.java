package com.flowable.gsd.work.configuration;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2Configuration {

    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2ConsoleServlet() {
        ServletRegistrationBean<JakartaWebServlet> bean = new ServletRegistrationBean<>(new JakartaWebServlet(), "/h2-console/*");
        bean.addInitParameter("webAllowOthers", "false");
        return bean;

    }
}