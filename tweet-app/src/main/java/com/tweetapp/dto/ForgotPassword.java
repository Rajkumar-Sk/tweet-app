package com.tweetapp.dto;

public class ForgotPassword {

    private String username;
    private String secretKey;
    private String newPassword;

    public ForgotPassword() {
    }

    public ForgotPassword(String username, String secretKey, String newPassword) {
        this.username = username;
        this.secretKey = secretKey;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
