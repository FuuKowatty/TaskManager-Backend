package pl.bartoszmech.domain.accountidentifier;

import lombok.Builder;

@Builder
record User(
        String id,
        String firstName,
        String lastName,
        String email,
        String password,
        UserRoles role
) {
}

