package pl.bartoszmech.domain.user;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import pl.bartoszmech.domain.user.dto.CreateUserRequestDto;
import pl.bartoszmech.domain.user.dto.UpdateUserRequestDto;
import pl.bartoszmech.domain.user.dto.UserDto;
import pl.bartoszmech.domain.shared.ResourceNotFound;

import java.util.List;

@AllArgsConstructor
@Service
class UserService {
    private static final String EMAIL_TAKEN = "User email is taken";
    private static final String USER_NOT_FOUND = "User with provided id could not be found";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User with provided email could not be found";

    private final UserRepository repository;

    UserDto findByEmail(String email) {
        return UserMapper.mapFromUser(repository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException(USER_NOT_FOUND_BY_EMAIL)));
    }
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