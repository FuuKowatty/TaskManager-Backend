package pl.bartoszmech.domain.user;

import pl.bartoszmech.domain.user.dto.UserDto;

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