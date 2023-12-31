package pl.bartoszmech.infrastructure.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartoszmech.domain.task.Task;

@Repository
public interface PostgreSQLTaskRepository extends JpaRepository<Task, Long> {
}
