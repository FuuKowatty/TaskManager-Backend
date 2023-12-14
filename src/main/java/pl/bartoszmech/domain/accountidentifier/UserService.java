package pl.bartoszmech.domain.accountidentifier;

import lombok.AllArgsConstructor;
import pl.bartoszmech.domain.accountidentifier.dto.CreateUserRequestDto;
import pl.bartoszmech.domain.accountidentifier.dto.UpdateUserRequestDto;
import pl.bartoszmech.domain.accountidentifier.dto.UserDto;
import pl.bartoszmech.domain.shared.ResourceNotFound;
import pl.bartoszmech.domain.task.dto.TaskDto;

import java.util.List;

@AllArgsConstructor
class UserService {
    private static final String EMAIL_TAKEN = "User email is taken";
    private static final String USER_NOT_FOUND = "User with provided id could not be found";

    private final UserRepository repository;

    UserDto createUser(CreateUserRequestDto inputUser) {
        User savedUser = repository.save(User.builder()
                .firstName(inputUser.firstName())
                .lastName(inputUser.lastName())
                .email(inputUser.email())
                .password(inputUser.password())
                .role(inputUser.role())
                .build());
        return UserMapper.mapFromUser(savedUser);
    }


    public List<UserDto> listUsers() {
        return repository
                .findAll()
                .stream()
                .map(user -> UserMapper.mapFromUser(user))
                .toList();
    }

    public UserDto findById(String id) {
        User foundUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND));
        return UserMapper.mapFromUser(foundUser);
    }

    public UserDto deleteById(String id) {
        User deletedUser = repository.deleteById(id)
                .orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND));
        return UserMapper.mapFromUser(deletedUser);
    }

    public UserDto updateUser(String id, UpdateUserRequestDto inputUser) {
        checkIfEmailIsAlreadyUsedByOtherUser(id, inputUser);
        User newUser = User.builder()
                .firstName(inputUser.firstName())
                .lastName(inputUser.lastName())
                .email(inputUser.email())
                .password(inputUser.password())
                .role(inputUser.role())
                .build();
        return UserMapper.mapFromUser(repository.update(id, newUser));
    }

    void checkIfEmailIsAlreadyUsedByOtherUser(String id, UpdateUserRequestDto userRequestDto) {
        if(findById(id).email() != userRequestDto.email() && repository.existsByEmail(userRequestDto.email())) {
            throw new EmailTakenException(EMAIL_TAKEN);
        }
    }

    void checkIfEmailIsAlreadyUsed(String email) {
        if(repository.existsByEmail(email)) {
            throw new EmailTakenException(EMAIL_TAKEN);
        }
    }
}
