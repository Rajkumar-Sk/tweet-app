package com.tweetapp.dto;

import javax.validation.constraints.NotBlank;

public class TweetAppUserDto {
    @NotBlank(message = "First Name should not be empty")
    private String firstNameDto;
    @NotBlank(message = "Last Name should not be empty")
    private String lastNameDto;
    @NotBlank(message = "Email should not be empty")
    private String emailDto;
    @NotBlank(message = "Login ID should not be empty")
    private String loginIdDto;
    @NotBlank(message = "Password should not be empty")
    private String passwordDto;
    @NotBlank(message = "Confirm Password should not be empty")
    private String confirmPasswordDto;
    @NotBlank(message = "Contact Number should not be empty")
    private String contactNumberDto;
    @NotBlank(message = "Secret Key should not be empty")
    private String secretKeyDto;


    public String getFirstNameDto() {
        return firstNameDto;
    }

    public void setFirstNameDto(String firstNameDto) {
        this.firstNameDto = firstNameDto;
    }

    public String getLastNameDto() {
        return lastNameDto;
    }

    public void setLastNameDto(String lastNameDto) {
        this.lastNameDto = lastNameDto;
    }

    public String getEmailDto() {
        return emailDto;
    }

    public void setEmailDto(String emailDto) {
        this.emailDto = emailDto;
    }

    public String getLoginIdDto() {
        return loginIdDto;
    }

    public void setLoginIdDto(String loginIdDto) {
        this.loginIdDto = loginIdDto;
    }

    public String getPasswordDto() {
        return passwordDto;
    }

    public void setPasswordDto(String passwordDto) {
        this.passwordDto = passwordDto;
    }

    public String getConfirmPasswordDto() {
        return confirmPasswordDto;
    }

    public void setConfirmPasswordDto(String confirmPasswordDto) {
        this.confirmPasswordDto = confirmPasswordDto;
    }

    public String getContactNumberDto() {
        return contactNumberDto;
    }

    public void setContactNumberDto(String contactNumberDto) {
        this.contactNumberDto = contactNumberDto;
    }

    public String getSecretKeyDto() {
        return secretKeyDto;
    }

    public void setSecretKeyDto(String secretKeyDto) {
        this.secretKeyDto = secretKeyDto;
    }
}