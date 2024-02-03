package pl.bartoszmech.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdatePasswordRequestDto(

        @NotNull(message = "Password is required.")
        @NotBlank(message = "Password must not be blank.")
        @Size(min = 6, message = "Password must be at least 6 characters long.")
        @Size(max = 50, message = "Password must not exceed 50 characters.")
        String newPassword,

        @NotNull(message = "Password is required.")
        @NotBlank(message = "Password must not be blank.")
        @Size(min = 6, message = "Password must be at least 6 characters long.")
        @Size(max = 50, message = "Password must not exceed 50 characters.")
        String newPasswordConfirmation,

        @NotNull(message = "Password is required.")
        @NotBlank(message = "Password must not be blank.")
        @Size(min = 6, message = "Password must be at least 6 characters long.")
        @Size(max = 50, message = "Password must not exceed 50 characters.")
        String oldPassword

){}
