package com.flowable.training.handson.design.enhancer;

import com.flowable.design.engine.api.history.AppRevision;
import com.flowable.design.engine.api.runtime.Model;
import com.flowable.design.rest.service.api.DesignRestApiInterceptor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;
import com.flowable.training.handson.design.service.GitRepoPublisherService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class RevisionPublisherRestApiInterceptorEnhancer implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionPublisherRestApiInterceptorEnhancer.class);

    @Autowired
    GitRepoPublisherService gitRepoPublisherService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DesignRestApiInterceptor) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
                Object result;
                if(method.getName().equals("publishAppRevision")) {
                    LOGGER.debug("Calling original bean definition for: " + method.getName());

                    result = proxy.invoke(bean, args);
                    Optional<Model> appModel = Arrays.stream(args).filter(Model.class::isInstance).map(Model.class::cast).findFirst();
                    Optional<AppRevision> appRevision = Arrays.stream(args).filter(AppRevision.class::isInstance).map(AppRevision.class::cast).findFirst();
                    LOGGER.debug("Attempting to commit and push revision published to configured git repo");
                    try {
                        gitRepoPublisherService.commitAndPushChanges(appModel.get(), appRevision.get());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (GitAPIException e) {
                        throw new RuntimeException(e);
                    }
                    return result;
                } else {
                    result = proxy.invoke(bean, args);
                }
                return result;
            });
            return enhancer.create();
        }
        return bean;
    }
}
