package com.kutyrina.accountexchanger.service;

import com.kutyrina.accountexchanger.component.SessionHolderComponent;
import com.kutyrina.accountexchanger.dto.ClientChangePasswordRequest;
import com.kutyrina.accountexchanger.dto.ClientRequest;
import com.kutyrina.accountexchanger.dto.ClientResponse;
import com.kutyrina.accountexchanger.entity.Client;
import com.kutyrina.accountexchanger.repository.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionHolderComponent sessionHolderComponent;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder, SessionHolderComponent sessionHolderComponent) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionHolderComponent = sessionHolderComponent;
    }

    public ClientResponse addNewAccount(ClientRequest createClientRequest) {
        Client client = new Client();
        client.setLogin(createClientRequest.getLogin());
        String encodedPassword = passwordEncoder.encode(createClientRequest.getPassword());
        client.setPassword(encodedPassword);
        clientRepository.save(client);
        return new ClientResponse(client.getLogin());
    }

    public String changePassword(ClientChangePasswordRequest changePasswordRequest) {
        Client client = getClientIfPresent();
        String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        client.setPassword(encodedPassword);
        clientRepository.save(client);
        return new ClientResponse().getMassage("Password change successfully for: " + client.getLogin());
    }

    public Optional<Client> findClientByLogin(String login) {
        return clientRepository.findByLogin(login);
    }

    private Client getClientIfPresent() {
        return clientRepository.findByLogin(sessionHolderComponent.getCurrentUserLogin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "client not found"));
    }


}
