package pl.bartoszmech.domain.user.dto;

import lombok.Builder;
import pl.bartoszmech.domain.user.UserRoles;

@Builder
public record CreateUserRequestDto(
        String firstName,
        String lastName,
        String email,
        String password,
        UserRoles role
) {
}
