package pl.bartoszmech.domain.task;

import java.util.List;
import java.util.Optional;

interface TaskRepository {
    Task save(Task task);

    List<Task> findAll();

    Optional<Task> findById(String id);

    Optional<Task> deleteById(String id);
}
