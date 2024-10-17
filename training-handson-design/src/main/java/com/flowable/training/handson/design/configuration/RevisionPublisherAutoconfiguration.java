package com.flowable.training.handson.design.configuration;

import com.flowable.design.engine.api.ModelType;
import com.flowable.design.engine.api.history.AppRevision;
import com.flowable.design.engine.api.history.ModelHistory;
import com.flowable.design.engine.api.management.Workspace;
import com.flowable.design.engine.api.runtime.Model;
import com.flowable.design.rest.service.api.DesignRestApiInterceptor;
import com.flowable.design.rest.service.api.model.*;
import com.flowable.design.rest.service.api.model._import.ImportModelRequest;
import com.flowable.design.rest.service.api.model.clone.ModelDuplicateRequest;
import com.flowable.design.rest.service.api.model.history.AppRevisionCreateRequest;
import com.flowable.design.rest.service.api.model.history.ModelHistoryActionRequest;
import com.flowable.design.rest.service.api.model.translation.Translation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.flowable.training.handson.design.interceptor.BasicDesignRestApiInterceptor;

import java.util.Map;

@Service
@ConditionalOnProperty(prefix = "flowable.design.git.repo", name = "enabled", havingValue = "true")
public class RevisionPublisherAutoconfiguration {
    @ConditionalOnMissingBean(DesignRestApiInterceptor.class)
    @Bean
    public DesignRestApiInterceptor basicDesignRestApiInterceptor(){
        return new BasicDesignRestApiInterceptor();
    }
}
