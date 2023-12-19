package pl.bartoszmech.domain.user;

import lombok.AllArgsConstructor;
import pl.bartoszmech.domain.user.dto.CreateUserRequestDto;
import pl.bartoszmech.domain.user.dto.UpdateUserRequestDto;
import pl.bartoszmech.domain.user.dto.UserDto;

import java.util.List;

import static pl.bartoszmech.domain.user.UserRoles.ADMIN;

@AllArgsConstructor
public class UserFacade {
    UserService service;

    public UserDto findByEmail(String email) {
        return service.findByEmail(email);
    }
    public UserDto createUser(CreateUserRequestDto inputUser) {
        service.checkIfEmailIsAlreadyUsed(inputUser.email());
        return service.createUser(inputUser);
    }

    public List<UserDto> listUsers() {
        return service.listUsers();
    }

    public UserDto deleteById(Long id) {
        return service.deleteById(id);
    }

    public UserDto findById(Long id) {
        return service.findById(id);
    }

    public UserDto updateUser(Long id, UpdateUserRequestDto userRequestDto) {
        service.checkIfEmailIsAlreadyUsedByOtherUser(id, userRequestDto);
        return service.updateUser(id, userRequestDto);
    }

    public UserDto registerAdmin(CreateUserRequestDto inputUser) {
        service.checkIfEmailIsAlreadyUsed(inputUser.email());
        return service.createUser(CreateUserRequestDto.builder()
                    .firstName(inputUser.firstName())
                    .lastName(inputUser.lastName())
                    .email(inputUser.email())
                    .password(inputUser.password())
                    .role(ADMIN)
                    .build());
    }
}
