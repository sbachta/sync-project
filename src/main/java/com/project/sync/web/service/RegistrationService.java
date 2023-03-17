package com.project.sync.web.service;

import com.project.sync.helpers.CreateRepository;
import com.project.sync.helpers.Service;
import com.project.sync.models.RegistrationData;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RegistrationService implements Service<RegistrationData, Boolean> {

    private final CreateRepository<RegistrationData> createRepository;

    public RegistrationService(CreateRepository<RegistrationData> createRepository) {
        this.createRepository = createRepository;
    }

    @Override
    public Optional<Boolean> serve(RegistrationData registrationData) {
        return createRepository.create(registrationData);
    }
}
