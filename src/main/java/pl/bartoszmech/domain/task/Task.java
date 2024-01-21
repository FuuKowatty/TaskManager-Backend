package pl.bartoszmech.domain.task;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;
import static pl.bartoszmech.domain.task.TaskStatus.FAILED;


@Getter
@Entity
@Table(name = "tasks")
public class Task {

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
        this.completedAt = completedAt;
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
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", completedAt=" + completedAt +
                ", assignedTo=" + assignedTo +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, startDate, endDate, assignedTo);
    }

    public void complete(LocalDateTime now) {
        this.status = TaskStatus.COMPLETED;
        this.completedAt = now;
    }

    public void fail() {
        this.status = FAILED;
    }

}
