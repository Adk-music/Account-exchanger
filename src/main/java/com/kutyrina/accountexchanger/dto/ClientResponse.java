package com.kutyrina.accountexchanger.dto;

public class ClientResponse {

    private String login;
    private String password;


    public ClientResponse(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public ClientResponse(String login) {
        this.login = login;
    }

    public ClientResponse() {
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

    public String getMassage(String massage) {
        return massage;
    }

    @Override
    public String toString() {
        return "ClientResponse{" +
                "login='" + login + '\'' +
                '}';
    }
}
