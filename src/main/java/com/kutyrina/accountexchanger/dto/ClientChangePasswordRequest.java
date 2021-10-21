package com.kutyrina.accountexchanger.dto;

public class ClientChangePasswordRequest {

    private final String newPassword;

    public ClientChangePasswordRequest(String newPassword) {

        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
