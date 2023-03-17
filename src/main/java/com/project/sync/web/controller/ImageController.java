package com.project.sync.web.controller;

import com.project.sync.helpers.Service;
import com.project.sync.models.ImageData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    private final Service<ImageData, Boolean> imageUploadService;
    private final Service<ImageData, Boolean> imageViewService;
    private final Service<ImageData, Boolean> imageDeleteService;

    public ImageController(
            @Qualifier("ImageUploadService") Service<ImageData, Boolean> imageUploadService,
            @Qualifier("ImageViewService") Service<ImageData, Boolean> imageViewService,
            @Qualifier("ImageDeleteService") Service<ImageData, Boolean> imageDeleteService
    ) {
        this.imageUploadService = imageUploadService;
        this.imageViewService   = imageViewService;
        this.imageDeleteService = imageDeleteService;
    }

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestBody final ImageData body) {
        return imageUploadService.serve(body).map(
                present -> ResponseEntity.status(present ? HttpStatus.OK : HttpStatus.BAD_REQUEST).build()).orElse(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/view")
    public ResponseEntity view(@RequestBody final ImageData body) {
        return imageViewService.serve(body).map(
                present -> ResponseEntity.status(present ? HttpStatus.OK : HttpStatus.BAD_REQUEST).build()).orElse(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/delete")
    public ResponseEntity delete(@RequestBody final ImageData body) {
        return imageDeleteService.serve(body).map(
                present -> ResponseEntity.status(present ? HttpStatus.OK : HttpStatus.BAD_REQUEST).build()).orElse(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
