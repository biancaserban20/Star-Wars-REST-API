package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
    @NotBlank(message = "User must not be blank")
        @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9._-]{2,30}$", message = "Username format is invalid")
        String user) {}
