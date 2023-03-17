package com.project.sync.integration;


import com.github.tomakehurst.wiremock.client.WireMock;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.project.sync.tables.Users.USERS;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@SpringBootTest(properties = {
        "imgur.host=http://localhost:${wiremock.server.port}",
        "imgur.context=/context",
        "imgur.image-endpoint=/mockImage",
        "imgur.account-endpoint=/mockAccount"
})
public abstract class BaseIntegrationTest {

    @Autowired
    protected DSLContext db;
    @Autowired
    protected MockMvc    mockMvc;

    private final String responseFromAccount = "{\n" +
                                               "  \"id\": 48437714,\n" +
                                               "  \"url\": \"ghostinspector\",\n" +
                                               "  \"bio\": null,\n" +
                                               "  \"avatar\": null,\n" +
                                               "  \"reputation\": 0,\n" +
                                               "  \"reputation_name\": \"Neutral\",\n" +
                                               "  \"created\": 1481839668,\n" +
                                               "  \"pro_expiration\": false,\n" +
                                               "  \"user_follow\": {\n" +
                                               "    \"status\": false\n" +
                                               "  }\n" +
                                               "}";

    @BeforeEach
    void setUp() {
        db.deleteFrom(USERS).execute();

        stubFor(post(urlPathEqualTo("/context/mockImage")).willReturn(ok()));

        stubFor(get(urlPathEqualTo("/context/mockImage/lotsOfRandomStuff"))
                        .willReturn(ok()));

        stubFor(delete(urlPathEqualTo("/context/mockImage/lotsOfRandomStuff"))
                        .willReturn(ok()));

        stubFor(get(urlPathEqualTo("/context/mockAccount/someUserName"))
                        .willReturn(ok().withBody(responseFromAccount)
                                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
    }

    @AfterEach
    void tearDown() {
        db.deleteFrom(USERS).execute();

        WireMock.reset();
    }
}
