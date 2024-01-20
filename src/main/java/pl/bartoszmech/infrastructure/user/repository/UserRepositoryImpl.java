package pl.bartoszmech.infrastructure.user.repository;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.bartoszmech.domain.user.User;
import pl.bartoszmech.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
@Primary
public class UserRepositoryImpl implements UserRepository {
    private final PostgreSQLUserRepository repository;

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public User save(User inputUser) {
        return repository.save(inputUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
