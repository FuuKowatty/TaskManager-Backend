package pl.bartoszmech.domain.task;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
record Task(
        String id,
        String title,
        String description,
        boolean isCompleted,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
