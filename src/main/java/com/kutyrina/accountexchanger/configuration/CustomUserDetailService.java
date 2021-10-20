package com.kutyrina.accountexchanger.configuration;

import com.kutyrina.accountexchanger.entity.Client;
import com.kutyrina.accountexchanger.service.ClientService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final ClientService clientService;

    public CustomUserDetailService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientService.findClientByLogin(username).orElseThrow(RuntimeException::new);
        return new AuthUser(client);
    }
}
