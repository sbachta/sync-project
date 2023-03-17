package com.project.sync.web.service;

import com.github.javafaker.Faker;
import com.project.sync.helpers.Client;
import com.project.sync.helpers.ReadRepository;
import com.project.sync.helpers.Service;
import com.project.sync.models.ImageData;
import com.project.sync.models.UserData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImageViewServiceTest {

    private final ReadRepository<UserData>    validationRepository = mock(ReadRepository.class);
    @Qualifier("ViewClient")
    private final Client<ImageData, Boolean>  viewClient           = mock(Client.class);
    private final Service<ImageData, Boolean> subject              = new ImageViewService(
            validationRepository,
            viewClient
    );

    private final Faker     faker     = new Faker();
    private final ImageData imageData = ImageData.builder()
                                                 .userData(UserData.builder()
                                                                   .username(faker.name().username())
                                                                   .password(faker.internet().password())
                                                                   .build())
                                                 .imageInfo(faker.internet().image())
                                                 .build();

    @Test
    void shouldCallValidationRepoToAuthUser() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));

        subject.serve(imageData);

        verify(validationRepository).read(imageData.getUserData());
    }

    @Test
    void shouldReturnOptionalFalseIfUserFailsValidation() {
        when(validationRepository.read(any())).thenReturn(Optional.of(false));

        Optional<Boolean> actual = subject.serve(imageData);

        verifyNoInteractions(viewClient);
        assertThat(actual).isEqualTo(Optional.of(false));
    }

    @Test
    void shouldCallViewClientWithImage() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));
        when(viewClient.send(imageData)).thenReturn(Optional.of(true));

        subject.serve(imageData);

        verify(viewClient).send(imageData);
    }

    @Test
    void shouldReturnOptionalTrueWhenAllCallsSucceed() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));
        when(viewClient.send(imageData)).thenReturn(Optional.of(true));

        Optional<Boolean> actual = subject.serve(imageData);

        assertThat(actual).isEqualTo(Optional.of(true));
    }

    @Test
    void shouldReturnOptionalFalseOnViewFailed() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));
        when(viewClient.send(imageData)).thenReturn(Optional.of(false));

        Optional<Boolean> actual = subject.serve(imageData);

        assertThat(actual).isEqualTo(Optional.of(false));
    }
}