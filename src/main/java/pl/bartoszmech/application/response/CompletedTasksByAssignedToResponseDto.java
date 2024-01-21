package pl.bartoszmech.application.response;

public record CompletedTasksByAssignedToResponseDto(

        Long assignedTo,
        Integer numberOfCompletedTasks)

{}
