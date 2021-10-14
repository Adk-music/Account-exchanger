package com.kutyrina.accountexchanger.controller;


import com.kutyrina.accountexchanger.dto.ClientChangePasswordResponse;
import com.kutyrina.accountexchanger.dto.ClientRequest;
import com.kutyrina.accountexchanger.dto.ClientResponse;
import com.kutyrina.accountexchanger.entity.Client;
import com.kutyrina.accountexchanger.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ClientController {

    @Autowired
    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PostMapping("client")
    public ClientResponse addNewAccount(@RequestBody @Valid ClientRequest createClientRequest) {
        Client client = new Client();
        client.setLogin(createClientRequest.getLogin());
        client.setPassword(createClientRequest.getPassword());
        clientRepository.save(client);
        return new ClientResponse(client.getLogin());
    }

    @PatchMapping("client/{login}")
    public String changePassword(@PathVariable String login, @RequestBody @Valid ClientChangePasswordResponse changePasswordResponse) {
        Client client = getClientIfPresent(login);
        Optional<Client> optionalClient = clientRepository.findById(client.getId());
        if (optionalClient.isPresent()) {
            Client clientData = optionalClient.get();
            if (clientData.getPassword().contains(changePasswordResponse.getOldPassword())) {
                clientData.setPassword(changePasswordResponse.getNewPassword());
                clientRepository.save(clientData);
            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        return new ClientResponse().getMassage("Password change successfully for " + client.getLogin());
    }


    private Client getClientIfPresent(String login) {
        return clientRepository.findByLogin(login)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found!"));
    }

}
