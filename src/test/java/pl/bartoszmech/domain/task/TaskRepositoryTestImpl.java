package pl.bartoszmech.domain.task;

import java.util.concurrent.ConcurrentHashMap;

public class TaskRepositoryTestImpl {
    ConcurrentHashMap<String, Task> database = new ConcurrentHashMap<>();

}
