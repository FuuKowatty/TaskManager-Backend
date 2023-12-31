package pl.bartoszmech.infrastructure.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartoszmech.domain.user.User;

import java.util.Optional;

@Repository
public interface PostgreSQLUserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
