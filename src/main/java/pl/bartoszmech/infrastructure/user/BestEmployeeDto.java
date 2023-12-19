package pl.bartoszmech.infrastructure.user;

import pl.bartoszmech.domain.user.dto.UserDto;

public record BestEmployeeDto(UserDto user, Integer numberOfCompletedTasks) {
}
