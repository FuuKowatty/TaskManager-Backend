package pl.bartoszmech.domain.user.dto;

import lombok.Builder;
import pl.bartoszmech.domain.user.UserRoles;

@Builder
public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String password,
        UserRoles role
) {
}
