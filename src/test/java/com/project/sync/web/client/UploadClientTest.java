package com.project.sync.web.client;

import com.project.sync.helpers.Client;
import com.project.sync.models.ImageData;
import com.project.sync.models.UserData;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class UploadClientTest {

    private final String                hostname              = "https://imgur";
    private final String                contextPath           = "/context";
    private final String                uploadPath            = "/upload";
    private final RestTemplate          restTemplate          = new RestTemplate();
    private final MockRestServiceServer mockRestServiceServer = MockRestServiceServer
            .bindTo(restTemplate)
            .build();

    private final Client<ImageData, Boolean> subject = new UploadClient(hostname, contextPath, uploadPath, restTemplate);

    private final ImageData imageData = ImageData.builder()
                                                 .userData(UserData.builder()
                                                                   .username("user")
                                                                   .password("pass")
                                                                   .build())
                                                 .imageInfo("imageData")
                                                 .build();

    @Test
    void shouldSendRequestToImgur() {
        MultiValueMap<String, String> formData   = new LinkedMultiValueMap<>();
        formData.add("image", "imageData");

        mockRestServiceServer
                .expect(requestTo(hostname + contextPath + uploadPath))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"))
                .andExpect(header("Authorization", "Client-ID 6793794c2fd47e5"))
                .andExpect(content().formData(formData));

        subject.send(imageData);

        mockRestServiceServer.verify();
    }

    @Test
    void shouldReturnOptionalTrueOnSuccess() {
        mockRestServiceServer
                .expect(requestTo(hostname + contextPath + uploadPath))
                .andRespond(withSuccess());

        Optional<Boolean> actual = subject.send(imageData);

        assertThat(actual).isEqualTo(Optional.of(true));
    }

    @Test
    void shouldReturnOptionalFalseOnError() {
        mockRestServiceServer
                .expect(requestTo(hostname + contextPath + uploadPath))
                .andRespond(withBadRequest());

        Optional<Boolean> actual = subject.send(imageData);

        assertThat(actual).isEqualTo(Optional.of(false));
    }
}