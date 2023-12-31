package pl.bartoszmech.domain.user.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.domain.user.EmailTakenException;
import pl.bartoszmech.domain.user.User;
import pl.bartoszmech.domain.user.UserMapper;
import pl.bartoszmech.domain.user.dto.UserDto;
import pl.bartoszmech.domain.user.repository.UserRepository;
import pl.bartoszmech.infrastructure.apivalidation.ResourceNotFound;

import java.util.List;

import static pl.bartoszmech.domain.user.UserRoles.ADMIN;
import static pl.bartoszmech.domain.user.UserRoles.EMPLOYEE;

@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String EMAIL_TAKEN = "User email is taken";
    private static final String USER_NOT_FOUND = "User with provided id could not be found";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User with provided email could not be found";

    private final UserRepository repository;

    @Override
    public UserDto findByEmail(String email) {
        return UserMapper.mapFromUser(repository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException(USER_NOT_FOUND_BY_EMAIL)));
    }

    @Override
    public UserResponseDto createUser(CreateAndUpdateUserRequestDto inputUser) {
        checkIfEmailIsAlreadyUsed(inputUser.email());
        User savedUser = repository.save(new User(
                    inputUser.firstName(),
                    inputUser.lastName(),
                    inputUser.email(),
                    inputUser.password(),
                    inputUser.role()
                ));
        return UserMapper.mapToResponse(savedUser);
    }

    @Override
    public List<UserResponseDto> listUsers() {
        return repository
                .findAll()
                .stream()
                .map(UserMapper::mapToResponse)
                .toList();
    }

    @Override
    public List<UserResponseDto> listEmployees() {
        return listUsers().stream()
                .filter(user -> user.role() == EMPLOYEE)
                .toList();
    }

    @Override
    public UserResponseDto findById(Long id) {
        User foundUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND));
        return UserMapper.mapToResponse(foundUser);
    }

    @Override
    public UserResponseDto deleteById(Long id) {
        UserResponseDto deletedUser = findById(id);
        repository.deleteById(id);
        return deletedUser;
    }

    @Override
    public UserResponseDto updateUser(Long id, CreateAndUpdateUserRequestDto inputUser) {
        checkIfEmailIsAlreadyUsedByOtherUser(id, inputUser);
        return UserMapper.mapToResponse(repository.save(new User(
                id,
                inputUser.firstName(),
                inputUser.lastName(),
                inputUser.email(),
                inputUser.password(),
                inputUser.role()
        )));
    }

    @Override
    public UserResponseDto registerAdmin(CreateAndUpdateUserRequestDto inputUser) {
        checkIfEmailIsAlreadyUsed(inputUser.email());
        return createUser(CreateAndUpdateUserRequestDto.builder()
                .firstName(inputUser.firstName())
                .lastName(inputUser.lastName())
                .email(inputUser.email())
                .password(inputUser.password())
                .role(ADMIN)
                .build());
    }

    private void checkIfEmailIsAlreadyUsedByOtherUser(Long id, CreateAndUpdateUserRequestDto userRequestDto) {
        if(!findById(id).email().equals(userRequestDto.email())  && repository.existsByEmail(userRequestDto.email())) {
            throw new EmailTakenException(EMAIL_TAKEN);
        }
    }

    private void checkIfEmailIsAlreadyUsed(String email) {
        if(repository.existsByEmail(email)) {
            throw new EmailTakenException(EMAIL_TAKEN);
        }
    }
}
