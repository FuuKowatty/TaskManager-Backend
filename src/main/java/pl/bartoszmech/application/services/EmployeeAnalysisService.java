package pl.bartoszmech.application.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.CompletedTasksByAssignedToResponseDto;
import pl.bartoszmech.application.response.CompletedTasksStatisticResponseDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.infrastructure.apivalidation.ResourceNotFound;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class EmployeeAnalysisService {
    public List<CompletedTasksStatisticResponseDto> sortEmployeesByCompletedTasks(List<UserResponseDto> employees, List<CompletedTasksByAssignedToResponseDto>  tasks, int lastMonths) {
        List<CompletedTasksStatisticResponseDto> employeeStatisticDtoList = mapToEmployeeStatistics(tasks, employees);
        return sortEmployeeStatistics(employeeStatisticDtoList);
    }

    private List<CompletedTasksStatisticResponseDto> mapToEmployeeStatistics(List<CompletedTasksByAssignedToResponseDto> tasks, List<UserResponseDto> employees) {
        return tasks
                .stream()
                .map(task -> fetchUser(task, employees))
                .toList();
    }

    private CompletedTasksStatisticResponseDto fetchUser(CompletedTasksByAssignedToResponseDto task, List<UserResponseDto> employees) {
        UserResponseDto user = employees.stream()
                .filter(employee -> employee.id().equals(task.assignedTo()))
                .findFirst()
                .orElse(null);
        return new CompletedTasksStatisticResponseDto(user, task.numberOfCompletedTasks());
    }

    private List<CompletedTasksStatisticResponseDto> sortEmployeeStatistics(List<CompletedTasksStatisticResponseDto> employeeStatisticDtoList) {
        return employeeStatisticDtoList.stream()
                .sorted(Comparator.comparing(CompletedTasksStatisticResponseDto::numberOfCompletedTasks)
                        .reversed())
                .toList();
    }
}
