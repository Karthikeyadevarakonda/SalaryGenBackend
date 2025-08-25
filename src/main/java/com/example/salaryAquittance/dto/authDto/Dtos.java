package com.example.salaryAquittance.dto.authDto;


import com.example.salaryAquittance.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Dtos {
    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record LoginResponse(
            Long id,
            String token,
            String tokenType,
            String username,
            String role,
            long expiresInSeconds
    ) {}

    public record RegisterRequest(
            @NotBlank String username,
            @NotBlank String password,
            @NotNull Role role
    ) {}

    public record MeResponse(
            String username,
            Role role
    ) {}
}
