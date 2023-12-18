package pl.bartoszmech.domain.accountidentifier;

import pl.bartoszmech.domain.accountidentifier.dto.UserDto;

class UserMapper {
    static UserDto mapFromUser(User savedTask) {
        return UserDto
                .builder()
                .id(savedTask.getId())
                .firstName(savedTask.getFirstName())
                .lastName(savedTask.getLastName())
                .email(savedTask.getEmail())
                .password(savedTask.getPassword())
                .role(savedTask.getRole())
                .build();
    }
}
