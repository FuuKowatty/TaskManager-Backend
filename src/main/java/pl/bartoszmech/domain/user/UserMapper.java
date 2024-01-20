package pl.bartoszmech.domain.user;

import lombok.NoArgsConstructor;
import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.domain.user.dto.UserDto;

@NoArgsConstructor
public class UserMapper {
    public static UserDto mapFromUser(User savedTask) {
        return new UserDto(
                savedTask.getId(),
                savedTask.getFirstName(),
                savedTask.getLastName(),
                savedTask.getEmail(),
                savedTask.getPassword(),
                savedTask.getRole());
    }

    public static UserResponseDto mapToResponse(User user) {
        return new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }

    public static CreateAndUpdateUserRequestDto mapToCreateAndUpdateRequest(CreateAndUpdateUserRequestDto requestDto) {
        return CreateAndUpdateUserRequestDto.builder()
                .firstName(requestDto.firstName())
                .lastName(requestDto.lastName())
                .email(requestDto.email())
                .password(requestDto.password())
                .role(requestDto.role())
                .build();
    }
}
