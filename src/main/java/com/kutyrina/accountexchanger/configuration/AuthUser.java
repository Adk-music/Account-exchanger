package com.kutyrina.accountexchanger.configuration;

import com.kutyrina.accountexchanger.entity.Account;
import com.kutyrina.accountexchanger.entity.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthUser implements UserDetails {

    private final Long id;
    private final String login;
    private final String password;
    private final Set<Long> accountIds;

    public AuthUser(Client client) {
        this.id = client.getId();
        this.login = client.getLogin();
        this.password = client.getPassword();
        this.accountIds = client.getClientAccounts()
                .stream()
                .map(Account::getAccountNumber)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    public Set<Long> getAccountIds() {
        return accountIds;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
