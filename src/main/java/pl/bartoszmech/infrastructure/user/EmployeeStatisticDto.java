package pl.bartoszmech.infrastructure.user;

import pl.bartoszmech.application.response.UserResponseDto;

public record EmployeeStatisticDto(UserResponseDto user, Integer numberOfCompletedTasks) {
}
