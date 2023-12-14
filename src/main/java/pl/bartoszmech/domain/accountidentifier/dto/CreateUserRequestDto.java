package pl.bartoszmech.domain.accountidentifier.dto;

import lombok.Builder;
import pl.bartoszmech.domain.accountidentifier.UserRoles;

import java.time.LocalDateTime;

@Builder
public record CreateUserRequestDto(
        String firstName,
        String lastName,
        String email,
        String password,
        UserRoles role
) {
}
