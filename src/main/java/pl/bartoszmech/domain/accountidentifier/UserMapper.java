package pl.bartoszmech.domain.accountidentifier;

import pl.bartoszmech.domain.accountidentifier.dto.UserDto;

class UserMapper {
    static UserDto mapFromUser(User savedTask) {
        return UserDto
                .builder()
                .id(savedTask.id())
                .firstName(savedTask.firstName())
                .lastName(savedTask.lastName())
                .email(savedTask.email())
                .password(savedTask.password())
                .role(savedTask.role())
                .build();
    }
}
