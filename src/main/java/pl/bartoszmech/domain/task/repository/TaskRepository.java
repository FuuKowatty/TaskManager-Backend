package pl.bartoszmech.domain.task.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.bartoszmech.domain.task.Task;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public interface TaskRepository {
    Task save(Task entity);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    void deleteById(Long id);
}
