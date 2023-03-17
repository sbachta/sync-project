package com.project.sync.web.controller;

import com.github.javafaker.Faker;
import com.project.sync.helpers.Service;
import com.project.sync.models.ImgurAccountInfo;
import com.project.sync.models.UserData;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Service<UserData, ImgurAccountInfo> accountService;

    private final Faker  faker    = new Faker();
    private final String username = faker.name().username();
    private final String password = faker.internet().password();
    private final String body     = "{\n" +
                                    "    \"username\": \"" + username + "\",\n" +
                                    "    \"password\": \"" + password + "\"\n" +
                                    "}";

    @Test
    void shouldCallAccountServiceToGetAccountInfo() throws Exception {
        when(accountService.serve(any())).thenReturn(Optional.of(ImgurAccountInfo.builder().build()));

        mockMvc.perform(post("/account-info")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body));

        verify(accountService).serve(UserData.builder().password(password).username(username).build());
    }

    @Test
    void shouldReturn200WhenImageServiceIsSuccessful() throws Exception {
        @Language("JSON") final String responseBody = "{\n" +
                                                      "  \"id\": 5,\n" +
                                                      "  \"url\": \"someUrl\",\n" +
                                                      "  \"bio\": \"someBio\"\n" +
                                                      "}";

        when(accountService.serve(any())).thenReturn(Optional.of(ImgurAccountInfo.builder()
                                                                                 .id(5)
                                                                                 .url("someUrl")
                                                                                 .bio("someBio")
                                                                                 .build()));

        mockMvc.perform(post("/account-info")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(content().json(responseBody))
               .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenOptionalIsEmpty() throws Exception {
        when(accountService.serve(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/account-info")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
               .andExpect(status().isBadRequest());
    }

}