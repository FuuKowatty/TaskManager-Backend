package pl.bartoszmech.application.response;

import lombok.Builder;
@Builder
public record TokenResponseDto(
        String token,
        String email
) {
}
