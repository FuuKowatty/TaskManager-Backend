package pl.bartoszmech.domain.accountidentifier.dto;

import lombok.Builder;
import pl.bartoszmech.domain.accountidentifier.UserRoles;

import javax.management.relation.Role;
import java.time.LocalDateTime;

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
