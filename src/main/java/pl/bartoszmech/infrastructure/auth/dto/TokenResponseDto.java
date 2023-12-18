package pl.bartoszmech.infrastructure.auth.dto;

import lombok.Builder;
@Builder
public record TokenResponseDto(
        String token,
        String email,
        Long id

) {
}
