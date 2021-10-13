package com.kutyrina.accountexchanger.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Работа с клиентом.
 */
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String password;

    @OneToMany(mappedBy = "client")
    private Set<Account> clientAccounts;

    public Long getId() {
        return id;
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

}
