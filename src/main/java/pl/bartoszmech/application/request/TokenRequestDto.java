package pl.bartoszmech.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TokenRequestDto(

        @NotNull(message = "Email is required.")
        @NotBlank(message = "Email must not be blank.")
        @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Invalid email format.")
        @JsonProperty("email")
        String username,
        @NotNull(message = "Password is required.")
        @NotBlank(message = "Password must not be blank.")
        @Size(min = 6, message = "Password must be at least 6 characters long.")
        @Size(max = 50, message = "Password must not exceed 50 characters.")
        String password

) {
}
