package com.project.sync.web.service;

import com.github.javafaker.Faker;
import com.project.sync.helpers.CreateRepository;
import com.project.sync.helpers.Service;
import com.project.sync.models.RegistrationData;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    private final CreateRepository<RegistrationData> createRepository = mock(CreateRepository.class);
    private final Service<RegistrationData, Boolean> subject          = new RegistrationService(createRepository);

    private final Faker            faker            = new Faker();
    private final RegistrationData registrationData = RegistrationData.builder()
                                                                      .email(faker.internet().emailAddress())
                                                                      .password(faker.internet().password())
                                                                      .username(faker.name().username())
                                                                      .build();


    @Test
    void shouldCallRepositoryToSaveData() {
        subject.serve(registrationData);

        verify(createRepository).create(registrationData);
    }

    @Test
    void shouldReturnOptionalTrueOnSuccess() {
        when(createRepository.create(any())).thenReturn(Optional.of(true));

        Optional<Boolean> actual = subject.serve(registrationData);

        assertThat(actual).isEqualTo(Optional.of(true));
    }

    @Test
    void shouldReturnOptionalFalseOnError() {
        when(createRepository.create(any())).thenReturn(Optional.of(false));

        Optional<Boolean> actual = subject.serve(registrationData);

        assertThat(actual).isEqualTo(Optional.of(false));
    }
}