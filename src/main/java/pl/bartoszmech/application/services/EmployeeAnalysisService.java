package pl.bartoszmech.application.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartoszmech.application.response.CompletedTasksByAssignedtoResponseDto;
import pl.bartoszmech.application.response.CompletedTasksStatisticResponseDto;
import pl.bartoszmech.application.response.UserResponseDto;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class EmployeeAnalysisService {
    public List<CompletedTasksStatisticResponseDto> sortEmployeesByCompletedTasks(List<UserResponseDto> employees, List<CompletedTasksByAssignedtoResponseDto>  tasks, int lastMonths) {
        List<CompletedTasksStatisticResponseDto> employeeStatisticDtoList = mapToEmployeeStatistics(tasks, employees);
        return sortEmployeeStatistics(employeeStatisticDtoList);
    }

    private List<CompletedTasksStatisticResponseDto> mapToEmployeeStatistics(List<CompletedTasksByAssignedtoResponseDto> tasks, List<UserResponseDto> employees) {
        return tasks
                .stream()
                .map(task -> fetchUser(task, employees))
                .toList();
    }

    private CompletedTasksStatisticResponseDto fetchUser(CompletedTasksByAssignedtoResponseDto task, List<UserResponseDto> employees) {
        UserResponseDto user = employees.stream()
                .filter(employee -> employee.id().equals(task.assignedTo()))
                .findFirst()
                .orElseThrow();
        return new CompletedTasksStatisticResponseDto(user, task.numberOfCompletedTasks());
    }

    private List<CompletedTasksStatisticResponseDto> sortEmployeeStatistics(List<CompletedTasksStatisticResponseDto> employeeStatisticDtoList) {
        return employeeStatisticDtoList.stream()
                .sorted(Comparator.comparing(CompletedTasksStatisticResponseDto::numberOfCompletedTasks)
                        .reversed())
                .toList();
    }
}
