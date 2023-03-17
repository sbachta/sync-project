package com.project.sync.web.controller;

import com.project.sync.helpers.Service;
import com.project.sync.models.ImgurAccountInfo;
import com.project.sync.models.UserData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final Service<UserData, ImgurAccountInfo> accountService;

    public AccountController(
            final Service<UserData, ImgurAccountInfo> accountService
    ) {
        this.accountService = accountService;
    }

    @PostMapping("/account-info")
    public ResponseEntity<ImgurAccountInfo> accountInfo(@RequestBody final UserData body) {
        return accountService.serve(body).map(
                imgurAccountInfo -> ResponseEntity.status(HttpStatus.OK).body(imgurAccountInfo)
        ).orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
