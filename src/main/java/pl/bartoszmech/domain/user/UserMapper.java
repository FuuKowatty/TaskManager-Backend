package pl.bartoszmech.domain.user;

import lombok.NoArgsConstructor;
import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.application.response.TokenResponseDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.domain.user.dto.UserDto;
import pl.bartoszmech.infrastructure.auth.dto.JwtResponseDto;

import static pl.bartoszmech.domain.user.UserRoles.ADMIN;

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

    public static UserResponseDto mapToResponseFromDto(UserDto user) {
        return UserResponseDto.builder()
                .id(user.id())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .role(user.role())
                .build();
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

    public static CreateAndUpdateUserRequestDto mapToCreateAdminRequest(CreateAndUpdateUserRequestDto user) {
            return CreateAndUpdateUserRequestDto.builder()
                    .firstName(user.firstName())
                    .lastName(user.lastName())
                    .email(user.email())
                    .password(user.password())
                    .role(ADMIN)
                    .build();
    }

    public static TokenResponseDto mapToTokenResponse(String token) {
        return TokenResponseDto.builder()
                .token(token)
                .build();
    }

}
