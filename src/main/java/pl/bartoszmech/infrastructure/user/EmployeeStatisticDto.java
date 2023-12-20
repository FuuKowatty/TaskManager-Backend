package pl.bartoszmech.infrastructure.user;

import pl.bartoszmech.domain.user.dto.UserDto;

public record EmployeeStatisticDto(UserDto user, Integer numberOfCompletedTasks) {
}
