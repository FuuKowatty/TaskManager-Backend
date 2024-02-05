package pl.bartoszmech.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import pl.bartoszmech.domain.user.UserRoles;

@Builder
public record UpdateUserDto(

        @NotNull(message = "First name is required.")
        @NotBlank(message = "First name must not be blank.")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must be alphanumeric.")
        @Size(min = 2, message = "First name must be at least 2 characters long.")
        @Size(max = 50, message = "First name must not exceed 50 characters.")
        String firstName,
        @NotNull(message = "Last name is required.")
        @NotBlank(message = "Last name must not be blank.")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must be alphanumeric.")
        @Size(min = 2, message = "Last name must be at least 2 characters long.")
        @Size(max = 50, message = "Last name must not exceed 50 characters.")
        String lastName,
        @NotNull(message = "Email is required.")
        @NotBlank(message = "Email must not be blank.")
        @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Email must be a valid email address.")
        String email,
        @NotNull(message = "User role is required.")
        UserRoles role

) {
}
