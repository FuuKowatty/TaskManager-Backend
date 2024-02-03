package pl.bartoszmech.domain.user.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.application.request.UpdatePasswordRequestDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.domain.user.EmailTakenException;
import pl.bartoszmech.domain.user.User;
import pl.bartoszmech.domain.user.UserMapper;
import pl.bartoszmech.domain.user.dto.UserDto;
import pl.bartoszmech.domain.user.repository.UserRepository;
import pl.bartoszmech.infrastructure.apivalidation.ResourceNotFound;
import pl.bartoszmech.infrastructure.auth.UnauthorizedAccessException;

import java.util.List;

import static pl.bartoszmech.domain.user.UserRoles.EMPLOYEE;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String EMAIL_TAKEN = "User email is taken";
    private static final String USER_NOT_FOUND = "User with provided id could not be found";
    private static final String USER_NOT_FOUND_BY_EMAIL = "User with provided email could not be found";
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto findByEmail(String email) {
        return UserMapper.mapFromUser(repository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException(USER_NOT_FOUND_BY_EMAIL)));
    }

    @Override
    public UserResponseDto createUser(CreateAndUpdateUserRequestDto inputUser) {
        checkIfEmailIsAlreadyUsed(inputUser.email());
        User savedUser = saveUserWithEncodedPassword(inputUser);
        return UserMapper.mapToResponse(savedUser);
    }

    private User saveUserWithEncodedPassword(CreateAndUpdateUserRequestDto inputUser) {
        return repository.save(new User(
                inputUser.firstName(),
                inputUser.lastName(),
                inputUser.email(),
                passwordEncoder.encode(inputUser.password()),
                inputUser.role())
        );
    }

    private User updateUserWithoutCredentials(long userId, CreateAndUpdateUserRequestDto inputUser) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND));
        user.setFirstName(inputUser.firstName());
        user.setLastName(inputUser.lastName());
        user.setRole(inputUser.role());
        return user;
    }

    @Transactional
    @Override
    public void updatePassword(UserDto user, UpdatePasswordRequestDto passwords) {
        if(passwordEncoder.matches(passwords.oldPassword(), user.password())) {
            User foundUser = repository.findById(user.id())
                    .orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND));
            foundUser.setPassword(passwordEncoder.encode(passwords.newPassword()));
        } else {
            throw new UnauthorizedAccessException("Password does not match");
        }
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

    @Transactional
    @Override
    public UserResponseDto updateUser(Long id, CreateAndUpdateUserRequestDto inputUser) {
        checkIfEmailIsAlreadyUsedByOtherUser(id, inputUser);
        return UserMapper.mapToResponse(updateUserWithoutCredentials(id, inputUser));
    }

    @Override
    public UserResponseDto registerAdmin(CreateAndUpdateUserRequestDto inputUser) {
        checkIfEmailIsAlreadyUsed(inputUser.email());
        return createUser(UserMapper.mapToCreateAdminRequest(inputUser));
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
