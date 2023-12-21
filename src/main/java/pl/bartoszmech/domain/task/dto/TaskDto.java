package pl.bartoszmech.domain.task.dto;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskDto(
        Long id,
        String title,
        String description,
        boolean isCompleted,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long assignedTo
) {
}
