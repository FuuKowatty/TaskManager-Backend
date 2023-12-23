package pl.bartoszmech.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import pl.bartoszmech.domain.user.UserRoles;

@Builder
public record CreateAndUpdateUserRequestDto(
        @NotNull(message = "{user.firstName.required}")
        @NotBlank(message = "{user.firstName.not.blank}")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "{user.firstName.invalid}")
        @Size(min = 2, message = "{user.firstName.too.short}")
        @Size(max = 50, message = "{user.firstName.too.long}")
        String firstName,
        @NotNull(message = "{user.lastName.required}")
        @NotBlank(message = "{user.lastName.not.blank}")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "{user.lastName.invalid}")
        @Size(min = 2, message = "{user.lastName.too.short}")
        @Size(max = 50, message = "{user.lastName.too.long}")
        String lastName,
        @NotNull(message = "{user.email.required}")
        @NotBlank(message = "{user.email.not.blank}")
        @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "{user.email.invalid}")
        String email,
        @NotNull(message = "{user.password.required}")
        @NotBlank(message = "{user.password.not.blank}")
        @Size(min = 6, message = "{user.password.too.short}")
        @Size(max = 50, message = "{user.password.too.long}")
        String password,
        @NotNull(message = "{user.role.required}")
        UserRoles role
) {
}
