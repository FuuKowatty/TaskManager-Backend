package pl.bartoszmech.domain.task;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class TaskRepositoryTestImpl implements TaskRepository{
    ConcurrentHashMap<Long, Task> database = new ConcurrentHashMap<>();

        @Override
        public Task save(Task entity) {
            if (entity.getId() == null) {
                Random random = new Random();
                long id = random.nextLong();
                Task task = new Task(id, entity.getTitle(), entity.getDescription(), entity.isCompleted(), entity.getStartDate()
                        ,entity.getEndDate(), entity.getAssignedTo());
                database.put(id, task);
                return task;
            } else {
                Task task = new Task(entity.getId(), entity.getTitle(), entity.getDescription(), entity.isCompleted(), entity.getStartDate()
                        ,entity.getEndDate(), entity.getAssignedTo());
                database.replace(entity.getId(), task);
                return task;
            }
        }
    @Override
    public Optional<Task> findById(Long id) {
        return database.values().stream().filter(task -> task.getId() == id).findFirst();
    }

    @Override
    public List<Task> findAll() {
            return database.values().stream().toList();
    }
    @Override
    public <S extends Task> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Task> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Task> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void deleteById(Long id) {
        database.remove(id);
    }

    @Override
    public void delete(Task entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Task> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Task> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Task> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Task> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Task getOne(Long aLong) {
        return null;
    }

    @Override
    public Task getById(Long aLong) {
        return null;
    }

    @Override
    public Task getReferenceById(Long aLong) {
        return null;
    }



    @Override
    public <S extends Task> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }


    @Override
    public <S extends Task> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Task> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Task, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Task> List<S> saveAll(Iterable<S> entities) {
        return null;
    }
    @Override
    public List<Task> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<Task> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Task> findAll(Pageable pageable) {
        return null;
    }
}
