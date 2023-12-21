package pl.bartoszmech.infrastructure.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartoszmech.domain.task.TaskFacade;
import pl.bartoszmech.domain.task.dto.CompletedTasksByAssignedtoResponseDto;
import pl.bartoszmech.domain.user.UserFacade;
import pl.bartoszmech.domain.user.dto.UserDto;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class EmployeeStatisticService {
    TaskFacade taskFacade;
    UserFacade userFacade;

    public List<EmployeeStatisticDto> sortEmployeesByCompletedTasks(int lastMonths) {
        List<CompletedTasksByAssignedtoResponseDto> completedTasksOfEachEmployeeFromLastSixMonths = taskFacade.getCompletedTasksByAssignedTo(lastMonths);
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
                    UserDto user = userFacade.findById(task.assignedTo());
                    return new EmployeeStatisticDto(user, task.numberOfCompletedTasks());
                })
                .toList();
    }
}
