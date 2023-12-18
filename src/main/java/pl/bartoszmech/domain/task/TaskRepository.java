package pl.bartoszmech.domain.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface TaskRepository extends JpaRepository<Task, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.isCompleted = true WHERE t.id = :taskId")
    void markTaskAsCompleted(Long taskId);
}
