package pl.bartoszmech.domain.accountidentifier;

import java.util.List;
import java.util.Optional;

interface UserRepository {
    User save(User user);

    List<User> findAll();

    Optional<User> findById(String id);

    Optional<User> deleteById(String id);

    User update(String id, User newUser);

    boolean existsByEmail(String email);
}
