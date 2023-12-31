package pl.bartoszmech.domain.user.service;

import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.domain.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto findByEmail(String email);
    UserDto createUser(CreateAndUpdateUserRequestDto inputUser);
    List<UserDto> listUsers();
    UserDto deleteById(Long id);
    UserDto findById(Long id);
    UserDto updateUser(Long id, CreateAndUpdateUserRequestDto userRequestDto);
    UserDto registerAdmin(CreateAndUpdateUserRequestDto inputUser);
}
