package pl.bartoszmech.domain.task;

import java.util.List;

interface TaskRepository {
    Task save(Task task);

    List<Task> findAll();
}
