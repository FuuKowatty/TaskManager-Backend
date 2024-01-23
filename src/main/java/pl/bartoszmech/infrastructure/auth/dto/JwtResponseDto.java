package pl.bartoszmech.infrastructure.auth.dto;

import lombok.Builder;

@Builder
public record JwtResponseDto(

        String username,
        String token

) {}
