package pl.bartoszmech.application.response;

public record CompletedTasksStatisticResponseDto(UserResponseDto user, Integer numberOfCompletedTasks) {
}
