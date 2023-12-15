package pl.bartoszmech.domain.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface TaskRepository extends JpaRepository<Task, Long> {
}
