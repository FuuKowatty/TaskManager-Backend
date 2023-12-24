package pl.bartoszmech.infrastructure.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TokenRequestDto(
        @NotNull(message = "{user.email.required}")
        @NotBlank(message = "{user.email.not.blank}")
        @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "{user.email.invalid}")
        @JsonProperty("email") String username,
        @NotNull(message = "{user.password.required}")
        @NotBlank(message = "{user.password.not.blank}")
        @Size(min = 6, message = "{user.password.too.short}")
        @Size(max = 50, message = "{user.password.too.long}")
        String password
) {
}
