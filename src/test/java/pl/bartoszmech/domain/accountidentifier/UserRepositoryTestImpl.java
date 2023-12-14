package pl.bartoszmech.domain.accountidentifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryTestImpl implements UserRepository {
    ConcurrentHashMap<String, User> database = new ConcurrentHashMap<>();

    @Override
    public User save(User newUser) {
        String id = UUID.randomUUID().toString();
        User user = User.builder()
                .id(id)
                .firstName(newUser.firstName())
                .lastName(newUser.lastName())
                .email(newUser.email())
                .password(newUser.password())
                .role(newUser.role())
                .build();
        database.put(id, user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return database.values().stream().toList();
    }

    @Override
    public Optional<User> findById(String id) {
        return database.values().stream().filter(task -> task.id() == id).findFirst();
    }

    @Override
    public Optional<User> deleteById(String id) {
        return Optional.ofNullable(database.remove(id));
    }

    @Override
    public User update(String id, User newUser) {
        User user = User.builder()
                .id(id)
                .firstName(newUser.firstName())
                .lastName(newUser.lastName())
                .email(newUser.email())
                .password(newUser.password())
                .role(newUser.role())
                .build();
        database.replace(id, user);
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return !database.values().stream().filter(user -> user.email() == email).toList().isEmpty();
    }
}


