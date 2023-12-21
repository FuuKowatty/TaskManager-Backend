package pl.bartoszmech.domain.task.dto;

public record CompletedTasksByAssignedtoResponseDto(Long assignedTo, Integer numberOfCompletedTasks) {
}
