package com.flowable.training.handson.work.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
public class StaticResourceConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(10)
                .addResourceHandler("/ext/*.js", "/*/ext/*.js")
                .addResourceLocations("classpath:/static/ext/", "classpath:/public/ext/")
                .setCacheControl(CacheControl.noCache());

        registry.setOrder(20)
                .addResourceHandler("/ext/*.css", "/*/ext/*.css")
                .addResourceLocations("classpath:/static/ext/", "classpath:/public/ext/")
                .setCacheControl(CacheControl.noCache());

        registry.setOrder(30)
                .addResourceHandler("/*.js")
                .addResourceLocations("classpath:/public/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(40)
                .addResourceHandler("/*.css")
                .addResourceLocations("classpath:/public/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(50)
                .addResourceHandler("/*.woff")
                .addResourceLocations("classpath:/public/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(60)
                .addResourceHandler("/*.woff2")
                .addResourceLocations("classpath:/public/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(70)
                .addResourceHandler("/*.svg")
                .addResourceLocations("classpath:/public/", "classpath:/public/twemoji/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(80)
                .addResourceHandler("/*.map")
                .addResourceLocations("classpath:/public/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(90)
                .addResourceHandler("/*.png")
                .addResourceLocations("classpath:/public/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(100)
                .addResourceHandler("/*.ico")
                .addResourceLocations("classpath:/public/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }
}