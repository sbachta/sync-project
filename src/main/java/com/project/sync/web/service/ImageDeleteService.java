package com.project.sync.web.service;

import com.project.sync.helpers.Client;
import com.project.sync.helpers.ReadRepository;
import com.project.sync.helpers.Service;
import com.project.sync.models.ImageData;
import com.project.sync.models.UserData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("ImageDeleteService")
public class ImageDeleteService implements Service<ImageData, Boolean> {

    private final ReadRepository<UserData>   validationRepository;
    private final Client<ImageData, Boolean> deleteClient;

    public ImageDeleteService(
            final ReadRepository<UserData> validationRepository,
            @Qualifier("DeleteClient") final Client<ImageData, Boolean> deleteClient
    ) {

        this.validationRepository = validationRepository;
        this.deleteClient         = deleteClient;
    }

    @Override
    public Optional<Boolean> serve(ImageData imageData) {
        return validationRepository.read(imageData.getUserData()).filter(validated -> validated).map(
                validated -> deleteClient.send(imageData)).orElse(Optional.of(false));
    }
}
