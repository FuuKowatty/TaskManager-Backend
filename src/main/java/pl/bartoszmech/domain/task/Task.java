package pl.bartoszmech.domain.task;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
record Task(
        String id,
        String title,
        String description,
        boolean isCompleted,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return isCompleted == task.isCompleted && Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(startDate, task.startDate) && Objects.equals(endDate, task.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, isCompleted, startDate, endDate);
    }
}
