package pl.bartoszmech.infrastructure.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TokenRequestDto(
        @JsonProperty("email") String username,
        String password
) {
}
