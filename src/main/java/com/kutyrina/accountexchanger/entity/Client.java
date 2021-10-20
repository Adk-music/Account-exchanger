package com.kutyrina.accountexchanger.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Работа с клиентом.
 */
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;
    private String password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> clientAccounts;

    public Long getId() {
        return id;
    }

    public Set<Account> getClientAccounts() {
        return clientAccounts;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Client{" +
                "login='" + login + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(login, client.login) && Objects.equals(password, client.password) && Objects.equals(clientAccounts, client.clientAccounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, clientAccounts);
    }
}
