package pl.bartoszmech.infrastructure.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.CompletedTasksByAssignedtoResponseDto;
import pl.bartoszmech.domain.task.service.TaskService;
import pl.bartoszmech.domain.user.dto.UserDto;
import pl.bartoszmech.domain.user.service.UserService;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class EmployeeStatisticService {
    TaskService taskService;
    UserService userService;

    public List<EmployeeStatisticDto> sortEmployeesByCompletedTasks(int lastMonths) {
        List<CompletedTasksByAssignedtoResponseDto> completedTasksOfEachEmployeeFromLastSixMonths = taskService.getCompletedTasksByAssignedTo(lastMonths);
        List<EmployeeStatisticDto> employeeStatisticDtoList = getUsersToCompletedTasks(completedTasksOfEachEmployeeFromLastSixMonths);
        return employeeStatisticDtoList.stream()
                .sorted(Comparator.comparing(EmployeeStatisticDto::numberOfCompletedTasks)
                        .reversed())
                        .toList();
    }

    private List<EmployeeStatisticDto> getUsersToCompletedTasks(List<CompletedTasksByAssignedtoResponseDto> completedTasksByAssignedtoResponseDtoList) {
        return completedTasksByAssignedtoResponseDtoList
                .stream()
                .map(task -> {
                    UserDto user = userService.findById(task.assignedTo());
                    return new EmployeeStatisticDto(user, task.numberOfCompletedTasks());
                })
                .toList();
    }
}
