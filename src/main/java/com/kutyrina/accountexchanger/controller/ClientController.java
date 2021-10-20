package com.kutyrina.accountexchanger.controller;


import com.kutyrina.accountexchanger.dto.ClientChangePasswordRequest;
import com.kutyrina.accountexchanger.dto.ClientRequest;
import com.kutyrina.accountexchanger.dto.ClientResponse;
import com.kutyrina.accountexchanger.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ClientController {

    @Autowired
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("client")
    public ClientResponse addNewAccount(@RequestBody @Valid ClientRequest createClientRequest) {
        return clientService.addNewAccount(createClientRequest);
    }

    @PatchMapping("client/change-password")
    public String changePassword(@RequestBody @Valid ClientChangePasswordRequest changePasswordResponse) {
        return clientService.changePassword(changePasswordResponse);
    }


}
