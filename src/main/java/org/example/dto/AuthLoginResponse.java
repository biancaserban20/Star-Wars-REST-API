package org.example.dto;

public record AuthLoginResponse(String accessToken, String refreshToken, String user) {}
