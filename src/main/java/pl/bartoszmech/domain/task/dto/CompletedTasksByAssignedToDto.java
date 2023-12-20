package pl.bartoszmech.domain.task.dto;

public record CompletedTasksByAssignedToDto(Long assignedTo, Integer numberOfCompletedTasks) {
}
