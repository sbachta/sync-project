package com.project.sync.web.controller;

import com.project.sync.helpers.Service;
import com.project.sync.models.RegistrationData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final Service<RegistrationData, Boolean> registrationService;

    public RegisterController(
            Service<RegistrationData, Boolean> registrationService
    ) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody final RegistrationData body) {
        return registrationService.serve(body).map(
                present -> ResponseEntity.status(present ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).build()).orElse(
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
