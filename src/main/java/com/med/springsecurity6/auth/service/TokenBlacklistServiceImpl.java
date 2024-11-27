package com.med.springsecurity6.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService{


    private RedisTemplate<String, Object> redisTemplate;
    private JwtService jwtService;

    public TokenBlacklistServiceImpl(RedisTemplate<String, Object> redisTemplate, JwtService jwtService) {
        this.redisTemplate = redisTemplate;
        this.jwtService = jwtService;
    }

    @Override
    public void addToBlacklist(HttpServletRequest request) {
        String token = jwtService.extractTokenFromRequest(request);
        Date expiry = jwtService.extractExpiration(token);
        // Calculate the remaining time to expiration
        long expiration = expiry.getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set(token, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }

}