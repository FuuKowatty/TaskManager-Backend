package pl.bartoszmech.infrastructure.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record TokenRequestDto(
        @JsonProperty("email") String username,
        String password
) {
}
