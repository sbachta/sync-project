package com.project.sync.integration;

import com.project.sync.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.project.sync.tables.Users.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SyncProjectIntegrationTest {

    @Autowired
    protected DSLContext db;
    @Autowired
    protected MockMvc    mockMvc;

    @BeforeEach
    void setUp() {
        db.deleteFrom(USERS).execute();
    }

    @Test
    void shouldRegisterUserAndSaveInfoInDB() throws Exception {
        String registerBody = "{\n" +
                              "  \"username\": \"someUserName\",\n" +
                              "  \"password\": \"someSecretPassword\",\n" +
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
    void shouldUploadImageForRegisteredUser() {
        
    }

    @Test
    void shouldDeleteImageForRegisteredUser() {

    }
}

