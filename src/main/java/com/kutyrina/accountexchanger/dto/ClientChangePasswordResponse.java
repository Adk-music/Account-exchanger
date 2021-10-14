package com.kutyrina.accountexchanger.dto;

public class ClientChangePasswordResponse {

    private final String oldPassword;
    private String newPassword;

    public ClientChangePasswordResponse(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
