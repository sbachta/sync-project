package com.project.sync.web.service;

import com.project.sync.helpers.Client;
import com.project.sync.helpers.ReadRepository;
import com.project.sync.helpers.Service;
import com.project.sync.models.ImageData;
import com.project.sync.models.UserData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("ImageUploadService")
public class ImageUploadService implements Service<ImageData, Boolean> {

    private final ReadRepository<UserData>  validationRepository;
    private final Client<ImageData, Boolean> uploadClient;

    public ImageUploadService(
            ReadRepository<UserData> validationRepository,
            @Qualifier("UploadClient") Client<ImageData, Boolean> uploadClient
    ) {
        this.validationRepository = validationRepository;
        this.uploadClient         = uploadClient;
    }

    @Override
    public Optional<Boolean> serve(ImageData imageData) {
        return validationRepository.read(imageData.getUserData()).filter(validated -> validated).map(
                validated -> uploadClient.send(imageData)).orElse(Optional.of(false));
    }
}
