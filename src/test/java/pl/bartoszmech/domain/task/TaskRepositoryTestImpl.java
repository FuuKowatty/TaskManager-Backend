package pl.bartoszmech.domain.task;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRepositoryTestImpl implements TaskRepository{
    ConcurrentHashMap<String, Task> database = new ConcurrentHashMap<>();

    @Override
    public Task save(Task task) {
        database.put(UUID.randomUUID().toString(), task);
        return task;
    }
}
