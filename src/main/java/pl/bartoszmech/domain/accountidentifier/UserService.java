package pl.bartoszmech.domain.accountidentifier;

import lombok.AllArgsConstructor;
import pl.bartoszmech.domain.accountidentifier.dto.CreateUserRequestDto;
import pl.bartoszmech.domain.accountidentifier.dto.UpdateUserRequestDto;
import pl.bartoszmech.domain.accountidentifier.dto.UserDto;
import pl.bartoszmech.domain.shared.ResourceNotFound;

import java.util.List;

@AllArgsConstructor
class UserService {
    private static final String EMAIL_TAKEN = "User email is taken";
    private static final String USER_NOT_FOUND = "User with provided id could not be found";

    private final UserRepository repository;

    UserDto createUser(CreateUserRequestDto inputUser) {
        User savedUser = repository.save(new User(
                    inputUser.firstName(),
                    inputUser.lastName(),
                    inputUser.email(),
                    inputUser.password(),
                    inputUser.role()
                ));
        return UserMapper.mapFromUser(savedUser);
    }


    List<UserDto> listUsers() {
        return repository
                .findAll()
                .stream()
                .map(UserMapper::mapFromUser)
                .toList();
    }

    UserDto findById(Long id) {
        User foundUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND));
        return UserMapper.mapFromUser(foundUser);
    }

    UserDto deleteById(Long id) {
        UserDto deletedUser = findById(id);
        repository.deleteById(id);
        return deletedUser;
    }

    UserDto updateUser(Long id, UpdateUserRequestDto inputUser) {
        checkIfEmailIsAlreadyUsedByOtherUser(id, inputUser);
        return UserMapper.mapFromUser(repository.save(new User(
                id,
                inputUser.firstName(),
                inputUser.lastName(),
                inputUser.email(),
                inputUser.password(),
                inputUser.role()
        )));
    }

    void checkIfEmailIsAlreadyUsedByOtherUser(Long id, UpdateUserRequestDto userRequestDto) {
        if(!findById(id).email().equals(userRequestDto.email())  && repository.existsByEmail(userRequestDto.email())) {
            throw new EmailTakenException(EMAIL_TAKEN);
        }
    }

    void checkIfEmailIsAlreadyUsed(String email) {
        if(repository.existsByEmail(email)) {
            throw new EmailTakenException(EMAIL_TAKEN);
        }
    }
}
