package com.project.sync.web.service;

import com.github.javafaker.Faker;
import com.project.sync.helpers.Client;
import com.project.sync.helpers.ReadRepository;
import com.project.sync.helpers.Service;
import com.project.sync.models.ImgurAccountInfo;
import com.project.sync.models.UserData;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private final ReadRepository<UserData>            validationRepository = mock(ReadRepository.class);
    private final Client<UserData, ImgurAccountInfo>  accountClient        = mock(Client.class);
    private final Service<UserData, ImgurAccountInfo> subject              = new AccountService(
            validationRepository,
            accountClient
    );

    private final Faker    faker    = new Faker();
    private final UserData userData = UserData.builder()
                                              .username(faker.name().username())
                                              .password(faker.internet().password())
                                              .build();

    @Test
    void shouldCallValidationRepoToAuthUser() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));

        subject.serve(userData);

        verify(validationRepository).read(userData);
    }

    @Test
    void shouldReturnOptionalEmptyIfUserFailsValidation() {
        when(validationRepository.read(any())).thenReturn(Optional.of(false));

        Optional<ImgurAccountInfo> actual = subject.serve(userData);

        verifyNoInteractions(accountClient);
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @Test
    void shouldCallAccountClient() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));
        when(accountClient.send(userData)).thenReturn(Optional.of(ImgurAccountInfo.builder().build()));

        subject.serve(userData);

        verify(accountClient).send(userData);
    }

    @Test
    void shouldReturnOptionalWithAccountInfoWhenAllCallsSucceed() {
        final ImgurAccountInfo accountInfo = ImgurAccountInfo.builder().id(5).build();
        when(validationRepository.read(any())).thenReturn(Optional.of(true));
        when(accountClient.send(userData)).thenReturn(Optional.of(accountInfo));

        Optional<ImgurAccountInfo> actual = subject.serve(userData);

        assertThat(actual).isEqualTo(Optional.of(accountInfo));
    }

    @Test
    void shouldReturnOptionalFalseOnViewFailed() {
        when(validationRepository.read(any())).thenReturn(Optional.of(true));
        when(accountClient.send(userData)).thenReturn(Optional.empty());

        Optional<ImgurAccountInfo> actual = subject.serve(userData);

        assertThat(actual).isEqualTo(Optional.empty());
    }
}