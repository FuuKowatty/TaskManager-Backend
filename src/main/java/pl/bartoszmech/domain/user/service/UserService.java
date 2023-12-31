package pl.bartoszmech.domain.user.service;

import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.domain.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto findByEmail(String email);
    UserResponseDto createUser(CreateAndUpdateUserRequestDto inputUser);
    List<UserResponseDto> listUsers();
    List<UserResponseDto> listEmployees();
    UserResponseDto deleteById(Long id);
    UserResponseDto findById(Long id);
    UserResponseDto updateUser(Long id, CreateAndUpdateUserRequestDto userRequestDto);
    UserResponseDto registerAdmin(CreateAndUpdateUserRequestDto inputUser);
}
