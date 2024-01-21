package pl.bartoszmech.infrastructure.task.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.bartoszmech.domain.task.Task;
import pl.bartoszmech.domain.task.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class TaskRepositoryImpl implements TaskRepository {

    private final PostgreSQLTaskRepository repository;

    @Override
    public Task save(Task entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
}
