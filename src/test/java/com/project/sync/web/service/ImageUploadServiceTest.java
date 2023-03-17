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
import static org.mockito.Mockito.*;

class ImageUploadServiceTest {

    private final ReadRepository<ImageData>  validationRepository = mock(ReadRepository.class);
    @Qualifier("UploadClient")
    private final Client<ImageData, Boolean> uploadClient         = mock(Client.class);
    private final Service<ImageData, Boolean>     subject              = new ImageUploadService(
            validationRepository,
            uploadClient
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

        verify(validationRepository).read(imageData);
    }

    @Test
    void shouldReturnOptionalFalseIfUserFailsValidation() {
        when(validationRepository.read(any())).thenReturn(Optional.of(false));

        Optional<Boolean> actual = subject.serve(imageData);

        verifyNoInteractions(uploadClient);
        assertThat(actual).isEqualTo(Optional.of(false));
    }

    @Test
    void shouldCallUploadClientWithImage() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));
        when(uploadClient.send(imageData)).thenReturn(Optional.of(true));

        subject.serve(imageData);

        verify(uploadClient).send(imageData);
    }

    @Test
    void shouldReturnOptionalTrueWhenAllCallsSucceed() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));
        when(uploadClient.send(imageData)).thenReturn(Optional.of(true));

        Optional<Boolean> actual = subject.serve(imageData);

        assertThat(actual).isEqualTo(Optional.of(true));
    }

    @Test
    void shouldReturnOptionalFalseOnUploadFailed() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));
        when(uploadClient.send(imageData)).thenReturn(Optional.of(false));

        Optional<Boolean> actual = subject.serve(imageData);

        assertThat(actual).isEqualTo(Optional.of(false));
    }
}