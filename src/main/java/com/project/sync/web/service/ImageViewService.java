package com.project.sync.web.service;

import com.project.sync.helpers.Client;
import com.project.sync.helpers.ReadRepository;
import com.project.sync.helpers.Service;
import com.project.sync.models.ImageData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("ImageViewService")
public class ImageViewService implements Service<ImageData, Boolean> {

    private final ReadRepository<ImageData>  validationRepository;
    private final Client<ImageData, Boolean> viewClient;

    public ImageViewService(
            final ReadRepository<ImageData> validationRepository,
            @Qualifier("ViewClient") final Client<ImageData, Boolean> viewClient
    ) {
        this.validationRepository = validationRepository;
        this.viewClient         = viewClient;
    }

    @Override
    public Optional<Boolean> serve(ImageData imageData) {
        return validationRepository.read(imageData).filter(validated -> validated).map(
                validated -> viewClient.send(imageData)).orElse(Optional.of(false));
    }
}
