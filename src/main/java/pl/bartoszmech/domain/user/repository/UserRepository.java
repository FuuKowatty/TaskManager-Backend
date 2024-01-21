package pl.bartoszmech.domain.user.repository;

import pl.bartoszmech.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    User save(User newUser);
    boolean existsByEmail(String email);
    List<User> findAll();
    void deleteById(Long id);

}
