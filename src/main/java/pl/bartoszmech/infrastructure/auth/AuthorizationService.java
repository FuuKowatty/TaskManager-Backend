package pl.bartoszmech.infrastructure.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.bartoszmech.domain.accountidentifier.AccountIdentifierFacade;
import pl.bartoszmech.domain.accountidentifier.dto.UserDto;

import static pl.bartoszmech.domain.accountidentifier.UserRoles.EMPLOYEE;

@Service
@AllArgsConstructor
public class AuthorizationService {
    private static final String TASK_NOT_ASSIGNED_FOR_THIS_EMPLOYEE = "You dont have permission to read task with id: ";
    AccountIdentifierFacade userFacade;
    public void hasUserPermissionToReadTaskWithId(long taskId,  long assignedTo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userFacade.findByEmail(auth.getName());
        if (user.role().equals(EMPLOYEE) && assignedTo != user.id()) {
            throw new UnauthorizedAccessException(TASK_NOT_ASSIGNED_FOR_THIS_EMPLOYEE + taskId);
        }
    }
}
