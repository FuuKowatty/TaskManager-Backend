package pl.bartoszmech.domain.user;

import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.domain.user.dto.UserDto;

public class UserMapper {
    public static UserDto mapFromUser(User savedTask) {
        return new UserDto(
                savedTask.getId(),
                savedTask.getFirstName(),
                savedTask.getLastName(),
                savedTask.getEmail(),
                savedTask.getPassword(),
                savedTask.getRole());
    }
    public static UserResponseDto mapToResponse(User user) {
        return new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }
}
