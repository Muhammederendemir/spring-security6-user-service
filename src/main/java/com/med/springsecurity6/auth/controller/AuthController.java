package com.med.springsecurity6.auth.controller;

import com.med.springsecurity6.auth.model.AuthRequestDTO;
import com.med.springsecurity6.auth.model.JwtResponseDTO;
import com.med.springsecurity6.auth.service.JwtService;
import com.med.springsecurity6.auth.service.TokenBlacklistService;
import com.med.springsecurity6.refleshtoken.RefreshToken;
import com.med.springsecurity6.refleshtoken.model.RefreshTokenRequestDTO;
import com.med.springsecurity6.refleshtoken.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final  JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final  AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(JwtService jwtService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager, TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            return new JwtResponseDTO(jwtService.GenerateToken(authRequestDTO.getUsername()), refreshToken.getToken());


        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }


    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return new  JwtResponseDTO(accessToken,refreshTokenRequestDTO.getToken());
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Clear any session-related data if necessary
        tokenBlacklistService.addToBlacklist(request);
        return ResponseEntity.ok("logout success");
    }
}

