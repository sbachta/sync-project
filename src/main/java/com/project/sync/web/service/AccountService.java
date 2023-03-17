package com.project.sync.web.service;

import com.project.sync.helpers.Client;
import com.project.sync.helpers.ReadRepository;
import com.project.sync.helpers.Service;
import com.project.sync.models.ImgurAccountInfo;
import com.project.sync.models.UserData;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountService implements Service<UserData, ImgurAccountInfo> {

    private final ReadRepository<UserData>           validationRepository;
    private final Client<UserData, ImgurAccountInfo> accountClient;

    public AccountService(
            final ReadRepository<UserData> validationRepository,
            final Client<UserData, ImgurAccountInfo> accountClient
    ) {

        this.validationRepository = validationRepository;
        this.accountClient        = accountClient;
    }

    @Override
    public Optional<ImgurAccountInfo> serve(UserData userData) {
        return validationRepository.read(userData).filter(
                present -> present).flatMap(present -> accountClient.send(userData));
    }
}
