package pl.bartoszmech.domain.task.service;

import pl.bartoszmech.application.response.CompletedTasksByAssignedtoResponseDto;
import pl.bartoszmech.application.request.CreateAndUpdateTaskRequestDto;
import pl.bartoszmech.application.response.TaskInfoResponseDto;
import pl.bartoszmech.application.response.TaskResponseDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(CreateAndUpdateTaskRequestDto taskRequestDto);
    List<TaskResponseDto> listTasks();
    TaskResponseDto findById(long id);
    TaskResponseDto deleteById(long id);
    TaskResponseDto updateTask(long id, CreateAndUpdateTaskRequestDto taskRequestDto);
    List<TaskResponseDto> listEmployeeTasks(long id);
    TaskInfoResponseDto completeTask(long id);
    List<CompletedTasksByAssignedtoResponseDto> getCompletedTasksByAssignedTo(int lastMonths);
    void markAsFailedOutdatedTasks();
}
