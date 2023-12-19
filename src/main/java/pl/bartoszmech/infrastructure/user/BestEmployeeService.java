package pl.bartoszmech.infrastructure.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bartoszmech.domain.task.TaskFacade;
import pl.bartoszmech.domain.task.dto.CompletedTasksByAssignedToDto;
import pl.bartoszmech.domain.user.UserFacade;
import pl.bartoszmech.domain.user.dto.UserDto;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class BestEmployeeService {
    TaskFacade taskFacade;
    UserFacade userFacade;

    public List<BestEmployeeDto> getBestEmployee() {
        List<CompletedTasksByAssignedToDto> completedTasksByAssignedToDtoList = taskFacade.getCompletedTasksByAssignedTo();
        List<BestEmployeeDto> bestEmployeeDtoList = getUsersToCompletedTasks(completedTasksByAssignedToDtoList);
        return bestEmployeeDtoList.stream()
                .sorted(Comparator.comparing(BestEmployeeDto::numberOfCompletedTasks)
                        .reversed())
                        .toList();
    }

    private List<BestEmployeeDto> getUsersToCompletedTasks(List<CompletedTasksByAssignedToDto> completedTasksByAssignedToDtoList) {
        return completedTasksByAssignedToDtoList
                .stream()
                .map(task -> {
                    UserDto user = userFacade.findById(task.assignedTo());
                    return new BestEmployeeDto(user, task.numberOfCompletedTasks());
                })
                .toList();
    }
}
