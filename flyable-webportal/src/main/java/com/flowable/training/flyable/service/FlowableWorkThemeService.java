package com.flowable.training.flyable.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.flowable.training.flyable.exception.FlowableServiceException;

@Service
public class FlowableWorkThemeService {

    @Qualifier("adminRestClient")
    private final RestClient adminRestClient;

    public FlowableWorkThemeService(RestClient adminRestClient) {
        this.adminRestClient = adminRestClient;
    }

    @Cacheable("theme")
    public String getTheme() {
        // Use service account - no need to fetch again, hence why this method is cacheable
        try {
            return adminRestClient.get()
                    .uri("idm-api/current-user/theme.css")
                    .retrieve()
                    .body(String.class);
        } catch (HttpStatusCodeException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, ex.getStatusCode().value());
        } catch (RestClientException ex) {
            throw new FlowableServiceException("Error calling Flowable API: " + ex.getMessage(), ex, 500);
        }
    }

}
