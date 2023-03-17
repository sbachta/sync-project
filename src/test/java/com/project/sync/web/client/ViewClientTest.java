package com.project.sync.web.client;

import com.project.sync.helpers.Client;
import com.project.sync.models.ImageData;
import com.project.sync.models.UserData;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ViewClientTest {
    private final String                hostname              = "https://imgur";
    private final String                contextPath           = "/context";
    private final String                viewPath            = "/view";
    private final RestTemplate          restTemplate          = new RestTemplate();
    private final MockRestServiceServer mockRestServiceServer = MockRestServiceServer
            .bindTo(restTemplate)
            .build();

    private final Client<ImageData, Boolean> subject = new ViewClient(hostname, contextPath, viewPath, restTemplate);

    private final ImageData imageData = ImageData.builder()
                                                 .userData(UserData.builder()
                                                                   .username("user")
                                                                   .password("pass")
                                                                   .build())
                                                 .imageInfo("imageData")
                                                 .build();

    @Test
    void shouldSendRequestToImgur() {
        mockRestServiceServer
                .expect(requestTo(hostname + contextPath + viewPath + "/imageData"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Client-ID 6793794c2fd47e5"));

        subject.send(imageData);

        mockRestServiceServer.verify();
    }

    @Test
    void shouldReturnOptionalTrueOnSuccess() {
        mockRestServiceServer
                .expect(requestTo(hostname + contextPath + viewPath + "/imageData"))
                .andRespond(withSuccess());

        Optional<Boolean> actual = subject.send(imageData);

        assertThat(actual).isEqualTo(Optional.of(true));
    }

    @Test
    void shouldReturnOptionalFalseOnError() {
        mockRestServiceServer
                .expect(requestTo(hostname + contextPath + viewPath + "/imageData"))
                .andRespond(withBadRequest());

        Optional<Boolean> actual = subject.send(imageData);

        assertThat(actual).isEqualTo(Optional.of(false));
    }
}