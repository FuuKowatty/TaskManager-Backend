package pl.bartoszmech.domain.task;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;


@Getter
@Entity
@Table(name = "tasks")
class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime completedAt;
    private Long assignedTo;

    public Task(Long id, String title, String description, TaskStatus status, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime completedAt, Long assignedTo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignedTo = assignedTo;
    }

    public Task() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startDate, task.startDate) && Objects.equals(endDate, task.endDate) && Objects.equals(assignedTo, task.assignedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, startDate, endDate, assignedTo);
    }
}
