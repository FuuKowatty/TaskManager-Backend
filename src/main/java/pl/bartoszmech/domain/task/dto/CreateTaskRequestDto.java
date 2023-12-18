package pl.bartoszmech.domain.task.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateTaskRequestDto(
        String title,
        String description,
        LocalDateTime endDate,
        Long assignedTo
) {
}
