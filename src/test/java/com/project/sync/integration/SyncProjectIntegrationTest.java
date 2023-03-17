package com.project.sync.integration;

import com.project.sync.tables.records.UsersRecord;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.project.sync.tables.Users.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class SyncProjectIntegrationTest extends BaseIntegrationTest {

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @Override
    @AfterEach
    void tearDown() {
        super.tearDown();
    }

    @Test
    void shouldRegisterUserAndSaveInfoInDB() throws Exception {
        @Language("JSON") String registerBody = "{\n" +
                                                "  \"userData\": {\n" +
                                                "    \"username\": \"someUserName\",\n" +
                                                "    \"password\": \"someSecretPassword\"\n" +
                                                "  },\n" +
                                                "  \"email\": \"someTotallyReal@email.com\"\n" +
                                                "}";

        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registerBody))
                .andExpect(status().isCreated());

        UsersRecord actual = db.fetchOne(USERS, USERS.USERNAME.eq("someUserName"));

        assertThat(actual.getUsername()).isEqualTo("someUserName");
        assertThat(actual.getPassword()).isEqualTo("someSecretPassword");
        assertThat(actual.getEmail()).isEqualTo("someTotallyReal@email.com");
    }

    @Test
    void shouldUploadImageForRegisteredUser() throws Exception {
        db.insertInto(USERS, USERS.EMAIL, USERS.PASSWORD, USERS.USERNAME)
          .values("thisemail@address.com", "someSecretPassword", "someUserName")
          .execute();

        final String imageUploadBody = "{\n" +
                                 "  \"userData\": {\n" +
                                 "    \"username\": \"someUserName\",\n" +
                                 "    \"password\": \"someSecretPassword\"\n" +
                                 "  },\n" +
                                 "  \"imageInfo\": \"lotsOfRandomStuff\"\n" +
                                 "}";

        mockMvc.perform(post("/upload")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(imageUploadBody))
               .andExpect(status().isOk());

        verify(postRequestedFor(urlPathEqualTo("/context/mockImage"))
                       .withHeader("Authorization", equalTo("Client-ID 6793794c2fd47e5")));
    }

    @Test
    void shouldViewImageForRegisteredUser() throws Exception {
        db.insertInto(USERS, USERS.EMAIL, USERS.PASSWORD, USERS.USERNAME)
          .values("thisemail@address.com", "someSecretPassword", "someUserName")
          .execute();

        final String imageViewBody = "{\n" +
                                       "  \"userData\": {\n" +
                                       "    \"username\": \"someUserName\",\n" +
                                       "    \"password\": \"someSecretPassword\"\n" +
                                       "  },\n" +
                                       "  \"imageInfo\": \"lotsOfRandomStuff\"\n" +
                                       "}";

        mockMvc.perform(post("/view")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(imageViewBody))
               .andExpect(status().isOk());

        verify(getRequestedFor(urlPathEqualTo("/context/mockImage/lotsOfRandomStuff"))
                       .withHeader("Authorization", equalTo("Client-ID 6793794c2fd47e5")));
    }

    @Test
    void shouldDeleteImageForRegisteredUser() throws Exception {
        db.insertInto(USERS, USERS.EMAIL, USERS.PASSWORD, USERS.USERNAME)
          .values("thisemail@address.com", "someSecretPassword", "someUserName")
          .execute();

        final String imageDeleteBody = "{\n" +
                                       "  \"userData\": {\n" +
                                       "    \"username\": \"someUserName\",\n" +
                                       "    \"password\": \"someSecretPassword\"\n" +
                                       "  },\n" +
                                       "  \"imageInfo\": \"lotsOfRandomStuff\"\n" +
                                       "}";

        mockMvc.perform(post("/delete")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(imageDeleteBody))
               .andExpect(status().isOk());

        verify(deleteRequestedFor(urlPathEqualTo("/context/mockImage/lotsOfRandomStuff"))
                       .withHeader("Authorization", equalTo("Client-ID 6793794c2fd47e5")));
    }
}

