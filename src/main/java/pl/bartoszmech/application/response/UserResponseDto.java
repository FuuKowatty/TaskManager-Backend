package pl.bartoszmech.application.response;

import pl.bartoszmech.domain.user.UserRoles;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRoles role
) {
}
