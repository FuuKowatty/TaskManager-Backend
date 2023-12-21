package pl.bartoszmech.infrastructure.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.bartoszmech.domain.user.UserFacade;
import pl.bartoszmech.domain.user.UserRoles;
import pl.bartoszmech.domain.user.dto.UserDto;

import static pl.bartoszmech.domain.user.UserRoles.ADMIN;
import static pl.bartoszmech.domain.user.UserRoles.EMPLOYEE;

@Service
@AllArgsConstructor
public class AuthorizationService {
    private static final String TASK_NOT_ASSIGNED_FOR_THIS_EMPLOYEE = "You dont have permission to read task with id: ";
    public static final String TASK_NOT_ASSIGNED_TO_EMPLOYEE = "Invalid assignedTo, task should be assigned to user with role employee but was: ";
    private static final String ADMIN_CREATION_NOT_ALLOWED = "Admin cannot create other admin, please authenticate via valid endpoint";
    public static final String EMPLOYEE_TRYING_READ_NOT_HIS_TASKS = "You dont have permission to read tasks of employee with id: ";

    UserFacade userFacade;
    public void hasUserPermissionToReadTaskWithId(long taskId,  long assignedTo) {
        UserDto user = findAuthenticatedUser();
        if (user.role().equals(EMPLOYEE) && assignedTo != user.id()) {
            throw new UnauthorizedAccessException(TASK_NOT_ASSIGNED_FOR_THIS_EMPLOYEE + taskId);
        }
    }

    public void hasUserPermissionToReadTasksOfEmployee(long id) {
        UserDto user = findAuthenticatedUser();
        if (user.role().equals(EMPLOYEE) && id != user.id()) {
            throw new UnauthorizedAccessException(EMPLOYEE_TRYING_READ_NOT_HIS_TASKS + id);
        }
    }

    public void checkIfTaskAssignedToEmployee(Long assignedTo) {
        UserRoles role = userFacade.findById(assignedTo).role();
        if(!role.equals(EMPLOYEE)) {
            throw new UnauthorizedAccessException(TASK_NOT_ASSIGNED_TO_EMPLOYEE + role.getRoleName());
        }
    }

    public void checkIfUserWantsCreateAdmin(UserRoles role) {
        if(role.equals(ADMIN)) {
            throw new UnauthorizedAccessException(ADMIN_CREATION_NOT_ALLOWED);
        }
    }

    private UserDto findAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userFacade.findByEmail(auth.getName());
        return user;
    }
}
