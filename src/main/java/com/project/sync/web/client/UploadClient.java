package com.project.sync.web.client;

import com.project.sync.helpers.Client;
import com.project.sync.models.ImageData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component("UploadClient")
public class UploadClient implements Client<ImageData, Boolean> {

    private final String       hostname;
    private final String       contextPath;
    private final String       uploadPath;
    private final RestTemplate restTemplate;

    public UploadClient(
            @Value("${imgur.host}") final String hostname,
            @Value("${imgur.context}") final String contextPath,
            @Value("${imgur.image-endpoint}") final String uploadPath,
            final RestTemplate restTemplate
    ) {
        this.hostname = hostname;
        this.contextPath = contextPath;
        this.uploadPath = uploadPath;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Boolean> send(ImageData imageData) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("image", imageData.getImageInfo());

        try {
            return restTemplate.exchange(RequestEntity
                                          .post(UriComponentsBuilder.fromHttpUrl(hostname+contextPath).path(uploadPath).build().toUri())
                                          .header("Authorization", "Client-ID 6793794c2fd47e5")
                                          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                          .body(map), String.class).getStatusCode().is2xxSuccessful()
                   ? Optional.of(true)
                   : Optional.of(false);
        } catch (Exception e) {
            return Optional.of(false);
        }
    }
}
