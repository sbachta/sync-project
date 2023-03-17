package com.project.sync.web.client;

import com.project.sync.helpers.Client;
import com.project.sync.models.ImgurAccountInfo;
import com.project.sync.models.UserData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class AccountInfoClient implements Client<UserData, ImgurAccountInfo> {

    private final String       hostname;
    private final String       contextPath;
    private final String       accountPath;
    private final RestTemplate restTemplate;

    public AccountInfoClient(
            @Value("${imgur.host}") final String hostname,
            @Value("${imgur.context}") final String contextPath,
            @Value("${imgur.account-endpoint}") final String accountPath,
            final RestTemplate restTemplate
    ) {
        this.hostname     = hostname;
        this.contextPath  = contextPath;
        this.accountPath  = accountPath;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<ImgurAccountInfo> send(UserData userData) {
        try{
            return Optional.of(restTemplate.exchange(RequestEntity
                                                 .get(UriComponentsBuilder.fromHttpUrl(hostname + contextPath)
                                                              .path(accountPath)
                                                              .pathSegment(userData.getUsername())
                                                              .build()
                                                              .toUri())
                                                 .header("Authorization", "Client-ID 6793794c2fd47e5")
                                                 .build(), ImgurAccountInfo.class).getBody());
        } catch (Exception e){
            return Optional.empty();
        }
    }
}
