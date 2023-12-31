package pl.bartoszmech.domain.task;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import pl.bartoszmech.domain.task.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


public class TaskRepositoryTestImpl implements TaskRepository {
    ConcurrentHashMap<Long, Task> database = new ConcurrentHashMap<>();

    @Override
    public Task save(Task entity) {
        if (entity.getId() == null) {
            Random random = new Random();
            long id = random.nextLong();
            Task task = new Task(id, entity.getTitle(), entity.getDescription(), entity.getStatus(), entity.getStartDate()
                    ,entity.getEndDate(), null, entity.getAssignedTo());
            database.put(id, task);
            return task;
        }
        Task task = new Task(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getStatus(), entity.getStartDate()
                ,entity.getEndDate(), entity.getCompletedAt(),entity.getAssignedTo());
        database.replace(entity.getId(), task);
        return database.get(entity.getId());
    }
    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Task> findAll() {
            return database.values().stream().toList();
    }

    @Override
    public void deleteById(Long id) {
        database.remove(id);
    }

}
