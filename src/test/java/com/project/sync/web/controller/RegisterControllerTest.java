package com.project.sync.web.controller;

import com.github.javafaker.Faker;
import com.project.sync.helpers.Service;
import com.project.sync.models.RegistrationData;
import com.project.sync.models.UserData;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Service<RegistrationData, Boolean> registrationService;

    private final Faker  faker    = new Faker();
    private final String username = faker.name().username();
    private final String password = faker.internet().password();
    private final String email = faker.internet().emailAddress();
    private final String body  = "{\n" +
                                 "  \"userData\": {\n" +
                                 "    \"username\": \""+username+"\",\n" +
                                 "    \"password\": \""+password+"\"\n" +
                                 "  }" +
                                 ", \n" +
                                 "  \"email\": \"" + email + "\"\n" +
                                 "}";

    @Test
    void shouldCallRegistrationServiceToPassRegistrationData() throws Exception {
        when(registrationService.serve(any())).thenReturn(Optional.of(true));

        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body));

        verify(registrationService).serve(RegistrationData.builder()
                                                          .userData(UserData.builder()
                                                                            .username(username)
                                                                            .password(password)
                                                                            .build())
                                                          .email(email)
                                                          .build());
    }

    @Test
    void shouldReturn201WhenSuccessful() throws Exception {
        when(registrationService.serve(any())).thenReturn(Optional.of(true));

        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenUnableToRegister() throws Exception {
        when(registrationService.serve(any())).thenReturn(Optional.of(false));

        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenOptionalIsEmpty() throws Exception {
        when(registrationService.serve(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
               .andExpect(status().isBadRequest());
    }
}