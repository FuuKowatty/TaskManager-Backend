package pl.bartoszmech.domain.user.service;

import pl.bartoszmech.application.request.CreateUserDto;
import pl.bartoszmech.application.request.UpdatePasswordRequestDto;
import pl.bartoszmech.application.request.UpdateUserDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.domain.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findByEmail(String email);
    UserResponseDto createUser(CreateUserDto inputUser);
    void updatePassword(UserDto user, UpdatePasswordRequestDto passwords);
    List<UserResponseDto> listUsers();
    List<UserResponseDto> listEmployees();
    UserResponseDto deleteById(Long id);
    UserResponseDto findById(Long id);
    UserResponseDto updateUser(Long id, UpdateUserDto userRequestDto);
    UserResponseDto registerAdmin(CreateUserDto inputUser);

}
