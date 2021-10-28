package com.kutyrina.accountexchanger.account.service;

import com.kutyrina.accountexchanger.component.SessionHolderComponent;
import com.kutyrina.accountexchanger.dto.ClientChangePasswordRequest;
import com.kutyrina.accountexchanger.dto.ClientRequest;
import com.kutyrina.accountexchanger.entity.Client;
import com.kutyrina.accountexchanger.repository.ClientRepository;
import com.kutyrina.accountexchanger.service.ClientService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @InjectMocks
    ClientService clientService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    SessionHolderComponent sessionHolderComponent;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void addNewAccount() {
        String encodedPassword = "login";
        Client client = new Client();
        ClientRequest createClientRequest = new ClientRequest();
        createClientRequest.setLogin("login");
        createClientRequest.setPassword("login");

        doReturn(encodedPassword).when(passwordEncoder).encode(createClientRequest.getPassword());
        doReturn(client).when(clientRepository).save(any());

        clientService.addNewAccount(createClientRequest);

        verify(passwordEncoder).encode(createClientRequest.getPassword());
        verify(clientRepository).save(any());

    }

    @Test
    void changePassword() {
        String userLogin = "login";
        String encodedPassword = "newPassword";
        Client client = new Client();

        ClientChangePasswordRequest changePasswordRequest =
                new ClientChangePasswordRequest("newPassword");

        doReturn(userLogin).when(sessionHolderComponent).getCurrentUserLogin();
        doReturn(Optional.of(client)).when(clientRepository).findByLogin(userLogin);
        doReturn(encodedPassword).when(passwordEncoder).encode(changePasswordRequest.getNewPassword());
        doReturn(client).when(clientRepository).save(client);

        clientService.changePassword(changePasswordRequest);
        Assertions.assertThat(client.getPassword()).isEqualTo(encodedPassword);

        verify(sessionHolderComponent).getCurrentUserLogin();
        verify(clientRepository).findByLogin(userLogin);
        verify(passwordEncoder).encode(changePasswordRequest.getNewPassword());
        verify(clientRepository).save(client);
    }

    @Test
    void changePassword_Error_User_Not_Exists() {
        String userLogin = "login";

        ClientChangePasswordRequest changePasswordRequest =
                new ClientChangePasswordRequest("newPassword");

        doReturn(userLogin).when(sessionHolderComponent).getCurrentUserLogin();
        doReturn(Optional.empty()).when(clientRepository).findByLogin(userLogin);

        Assertions.assertThatThrownBy(() -> clientService.changePassword(changePasswordRequest))
                .isExactlyInstanceOf(ResponseStatusException.class);

        verify(sessionHolderComponent).getCurrentUserLogin();
        verify(clientRepository).findByLogin(userLogin);
    }


}
