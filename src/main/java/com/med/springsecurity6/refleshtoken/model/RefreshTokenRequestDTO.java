package com.med.springsecurity6.refleshtoken.model;

public class RefreshTokenRequestDTO {
    private String token;

    public RefreshTokenRequestDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}