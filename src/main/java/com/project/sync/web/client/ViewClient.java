package com.project.sync.web.client;

import com.project.sync.helpers.Client;
import com.project.sync.models.ImageData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component("ViewClient")
public class ViewClient implements Client<ImageData, Boolean> {

    private final String       hostname;
    private final String       contextPath;
    private final String       viewPath;
    private final RestTemplate restTemplate;

    public ViewClient(
            @Value("${imgur.host}") final String hostname,
            @Value("${imgur.context}") final String contextPath,
            @Value("${imgur.image-endpoint}") final String viewPath,
            final RestTemplate restTemplate
    ) {
        this.hostname     = hostname;
        this.contextPath  = contextPath;
        this.viewPath   = viewPath;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Boolean> send(ImageData imageData) {
        try {
            return restTemplate.exchange(RequestEntity
                                                 .get(UriComponentsBuilder.fromHttpUrl(hostname + contextPath)
                                                                           .path(viewPath)
                                                                           .pathSegment(imageData.getImageInfo())
                                                                           .build()
                                                                           .toUri())
                                                 .header("Authorization", "Client-ID 6793794c2fd47e5")
                                                 .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                                 .build(), String.class).getStatusCode().is2xxSuccessful()
                   ? Optional.of(true)
                   : Optional.of(false);
        } catch (Exception e) {
            return Optional.of(false);
        }
    }
}
