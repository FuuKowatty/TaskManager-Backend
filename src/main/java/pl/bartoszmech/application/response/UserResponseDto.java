package pl.bartoszmech.application.response;

import lombok.Builder;
import pl.bartoszmech.domain.user.UserRoles;

@Builder
public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        UserRoles role
) {
}
