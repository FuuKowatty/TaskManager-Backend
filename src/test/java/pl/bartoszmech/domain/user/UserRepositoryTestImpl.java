package pl.bartoszmech.domain.user;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import pl.bartoszmech.domain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class UserRepositoryTestImpl implements UserRepository {
    ConcurrentHashMap<Long, User> database = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findByEmail(String email) {
        return database.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }
    @Override
    public User save(User newUser) {
        if(newUser.getId() == null) {
            Random random = new Random();
            long id = random.nextLong();
            User user = new User(
                    id,
                    newUser.getFirstName(),
                    newUser.getLastName(),
                    newUser.getEmail(),
                    newUser.getPassword(),
                    newUser.getRole()
            );
            database.put(id, user);
            return user;
        }
        long id = newUser.getId();
        User user = new User(
                id,
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getEmail(),
                newUser.getPassword(),
                newUser.getRole()
        );
        database.replace(id, user);
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return !database.values().stream().filter(user -> user.getEmail().equals(email)).toList().isEmpty();
    }

    @Override
    public List<User> findAll() {
        return database.values().stream().toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return database.values().stream().filter(task -> task.getId().equals(id)).findFirst();
    }

    @Override
    public void deleteById(Long id) {
        database.remove(id);
    }
}


