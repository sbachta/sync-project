package com.project.sync.web.controller;

import com.github.javafaker.Faker;
import com.project.sync.helpers.Service;
import com.project.sync.models.ImageData;
import com.project.sync.models.UserData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@WebMvcTest(controllers = ImageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("ImageUploadService")
    private Service<ImageData, Boolean> imageUploadService;

    @MockBean
    @Qualifier("ImageViewService")
    private Service<ImageData, Boolean> imageViewService;

    @MockBean
    @Qualifier("ImageDeleteService")
    private Service<ImageData, Boolean> imageDeleteService;

    private final Faker  faker    = new Faker();
    private final String username = faker.name().username();
    private final String password = faker.internet().password();
    private final String image    = faker.internet().image();
    private final String body  = "{\n" +
                                 "  \"userData\": {\n" +
                                 "    \"username\": \""+username+"\",\n" +
                                 "    \"password\": \""+password+"\"\n" +
                                 "  }" +
                                 ", \n" +
                                 "  \"imageInfo\": \"" + image + "\"\n" +
                                 "}";

    @Nested
    class UploadImage {
        @Test
        void shouldCallImageServiceToPassImageData() throws Exception {
            when(imageUploadService.serve(any())).thenReturn(Optional.of(true));

            mockMvc.perform(post("/upload")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body));

            verify(imageUploadService).serve(ImageData.builder()
                                                      .imageInfo(image)
                                                      .userData(UserData.builder()
                                                                 .password(password)
                                                                 .username(username)
                                                                 .build())
                                                      .build());
        }

        @Test
        void shouldReturn200WhenImageServiceIsSuccessful() throws Exception {
            when(imageUploadService.serve(any())).thenReturn(Optional.of(true));

            mockMvc.perform(post("/upload")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                   .andExpect(status().isOk());
        }

        @Test
        void shouldReturn400WhenImageServiceIsUnsuccessful() throws Exception {
            when(imageUploadService.serve(any())).thenReturn(Optional.of(false));

            mockMvc.perform(post("/upload")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                   .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn400WhenOptionalIsEmpty() throws Exception {
            when(imageUploadService.serve(any())).thenReturn(Optional.empty());

            mockMvc.perform(post("/upload")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                   .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ViewImage {
        @Test
        void shouldCallImageServiceToPassImageData() throws Exception {
            when(imageViewService.serve(any())).thenReturn(Optional.of(true));

            mockMvc.perform(post("/view")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body));

            verify(imageViewService).serve(ImageData.builder()
                                                      .imageInfo(image)
                                                      .userData(UserData.builder()
                                                                  .password(password)
                                                                  .username(username)
                                                                  .build())
                                                      .build());
        }

        @Test
        void shouldReturn200WhenImageServiceIsSuccessful() throws Exception {
            when(imageViewService.serve(any())).thenReturn(Optional.of(true));

            mockMvc.perform(post("/view")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                   .andExpect(status().isOk());
        }

        @Test
        void shouldReturn400WhenImageServiceIsUnsuccessful() throws Exception {
            when(imageViewService.serve(any())).thenReturn(Optional.of(false));

            mockMvc.perform(post("/view")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                   .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn400WhenOptionalIsEmpty() throws Exception {
            when(imageViewService.serve(any())).thenReturn(Optional.empty());

            mockMvc.perform(post("/view")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                   .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteImage {
        @Test
        void shouldCallImageServiceToPassImageData() throws Exception {
            when(imageDeleteService.serve(any())).thenReturn(Optional.of(true));

            mockMvc.perform(post("/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body));

            verify(imageDeleteService).serve(ImageData.builder()
                                                      .imageInfo(image)
                                                      .userData(UserData.builder()
                                                                  .password(password)
                                                                  .username(username)
                                                                  .build())
                                                      .build());
        }

        @Test
        void shouldReturn200WhenImageServiceIsSuccessful() throws Exception {
            when(imageDeleteService.serve(any())).thenReturn(Optional.of(true));

            mockMvc.perform(post("/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                   .andExpect(status().isOk());
        }

        @Test
        void shouldReturn400WhenImageServiceIsUnsuccessful() throws Exception {
            when(imageDeleteService.serve(any())).thenReturn(Optional.of(false));

            mockMvc.perform(post("/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                   .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn400WhenOptionalIsEmpty() throws Exception {
            when(imageDeleteService.serve(any())).thenReturn(Optional.empty());

            mockMvc.perform(post("/delete")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                   .andExpect(status().isBadRequest());
        }
    }
}