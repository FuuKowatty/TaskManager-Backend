package pl.bartoszmech.application.response;

public record CompletedTasksByAssignedtoResponseDto(Long assignedTo, Integer numberOfCompletedTasks) {
}
