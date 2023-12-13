package pl.bartoszmech.domain.task;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRepositoryTestImpl implements TaskRepository{
    ConcurrentHashMap<String, Task> database = new ConcurrentHashMap<>();

    @Override
    public Task save(Task entity) {
        String id = UUID.randomUUID().toString();
        Task task = Task
                .builder()
                .id(id)
                .title(entity.title())
                .description(entity.description())
                .isCompleted(false)
                .startDate(entity.startDate())
                .endDate(entity.endDate())
                .build();
        database.put(id, task);
        return task;
    }

    @Override
    public List<Task> findAll() {
        return database.values().stream().toList();
    }

}
