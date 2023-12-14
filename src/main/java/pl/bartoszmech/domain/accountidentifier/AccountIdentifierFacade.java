package pl.bartoszmech.domain.accountidentifier;

import lombok.AllArgsConstructor;
import pl.bartoszmech.domain.accountidentifier.dto.CreateUserRequestDto;
import pl.bartoszmech.domain.accountidentifier.dto.UpdateUserRequestDto;
import pl.bartoszmech.domain.accountidentifier.dto.UserDto;

import java.util.List;

@AllArgsConstructor
public class AccountIdentifierFacade {
    UserService service;

    public UserDto createUser(CreateUserRequestDto inputUser) {
        service.checkIfEmailIsAlreadyUsed(inputUser.email());
        return service.createUser(inputUser);
    }

    public List<UserDto> listUsers() {
        return service.listUsers();
    }

    public UserDto deleteById(String id) {
        return service.deleteById(id);
    }

    public UserDto findById(String id) {
        return service.findById(id);
    }

    public UserDto updateUser(String id, UpdateUserRequestDto userRequestDto) {
        service.checkIfEmailIsAlreadyUsedByOtherUser(id, userRequestDto);
        return service.updateUser(id, userRequestDto);
    }
}
