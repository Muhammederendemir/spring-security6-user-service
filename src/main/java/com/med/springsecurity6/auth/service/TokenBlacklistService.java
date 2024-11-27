package com.med.springsecurity6.auth.service;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenBlacklistService {

    void addToBlacklist(HttpServletRequest request);
    Boolean isBlacklisted(String token);

}