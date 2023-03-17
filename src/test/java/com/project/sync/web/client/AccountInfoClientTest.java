package com.project.sync.web.client;

import com.project.sync.helpers.Client;
import com.project.sync.models.ImgurAccountInfo;
import com.project.sync.models.UserData;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AccountInfoClientTest {

    private final String                hostname              = "https://imgur";
    private final String                contextPath           = "/context";
    private final String                accountPath           = "/account";
    private final RestTemplate          restTemplate          = new RestTemplate();
    private final MockRestServiceServer mockRestServiceServer = MockRestServiceServer
            .bindTo(restTemplate)
            .build();

    private final Client<UserData, ImgurAccountInfo> subject = new AccountInfoClient(
            hostname, contextPath, accountPath, restTemplate);

    private final UserData         userData    = UserData.builder()
                                                         .username("user")
                                                         .password("pass")
                                                         .build();
    private final ImgurAccountInfo accountInfo = ImgurAccountInfo.builder()
                                                                 .bio("someBio")
                                                                 .id(5)
                                                                 .url("someUrl")
                                                                 .build();

    @Test
    void shouldSendRequestToImgur() {
        mockRestServiceServer
                .expect(requestTo(hostname + contextPath + accountPath + "/user"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Client-ID 6793794c2fd47e5"));

        subject.send(userData);

        mockRestServiceServer.verify();
    }

    @Test
    void shouldReturnOptionalOfAccountInfoOnSuccess() {
        final String imgurResponse = "{\n" +
                                     "  \"bio\": \"someBio\",\n" +
                                     "  \"id\": 5,\n" +
                                     "  \"url\": \"someUrl\"\n" +
                                     "}";
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.CONTENT_TYPE, singletonList(MediaType.APPLICATION_JSON_VALUE));

        mockRestServiceServer
                .expect(requestTo(hostname + contextPath + accountPath + "/user"))
                .andRespond(withSuccess().headers(httpHeaders).body(imgurResponse));

        Optional<ImgurAccountInfo> actual = subject.send(userData);

        assertThat(actual).isEqualTo(Optional.of(accountInfo));
    }

    @Test
    void shouldReturnOptionalEmptyOnError() {
        mockRestServiceServer
                .expect(requestTo(hostname + contextPath + accountPath + "/user"))
                .andRespond(withBadRequest());

        Optional<ImgurAccountInfo> actual = subject.send(userData);

        assertThat(actual).isEqualTo(Optional.empty());
    }
}