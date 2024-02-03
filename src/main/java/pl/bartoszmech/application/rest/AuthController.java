package pl.bartoszmech.application.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.application.request.UpdatePasswordRequestDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.application.services.AuthorizationService;
import pl.bartoszmech.domain.user.UserMapper;
import pl.bartoszmech.domain.user.service.UserService;
import pl.bartoszmech.infrastructure.apivalidation.ValidationResponse;
import pl.bartoszmech.infrastructure.auth.UnauthorizedAccessException;
import pl.bartoszmech.infrastructure.auth.dto.JwtResponseDto;
import pl.bartoszmech.application.request.TokenRequestDto;
import pl.bartoszmech.application.response.TokenResponseDto;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AuthController {

    private final AuthorizationService authorizationService;
    private final UserService userService;

    @Operation(summary = "Get token for authenticated requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResponse.class)))
    })
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> authenticateAndGenerateToken(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        final JwtResponseDto jwtDto = authorizationService.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.status(OK).body(UserMapper.mapToTokenResponse(jwtDto.token()));
    }

    @Operation(summary = "Create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created operation"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerAdmin(@Valid @RequestBody CreateAndUpdateUserRequestDto user) {
        CreateAndUpdateUserRequestDto inputAdmin = UserMapper.mapToCreateAdminRequest(user);
        return ResponseEntity.status(CREATED).body(userService.registerAdmin(inputAdmin));
    }

    @Operation(summary = "Get user data by token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned user data"),
            @ApiResponse(responseCode = "401", description = "Invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class)))
    })
    @GetMapping("/data")
    public ResponseEntity<UserResponseDto> getUserById() {
        return ResponseEntity.status(OK).body(authorizationService.findAuthenticatedUserWithoutPassword());
    }

    @Operation(summary = "Change password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return message and success status"),
            @ApiResponse(responseCode = "401", description = "Invalid token or password does not match",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class))),

    })
        @PatchMapping("/change-password")
    public ResponseEntity<Void> changeAccountPassword(@RequestBody @Valid UpdatePasswordRequestDto passwords) {
        authorizationService.checkIfPasswordMatch(passwords);
        userService.updatePassword(authorizationService.findAuthenticatedUser(), passwords);
        return ResponseEntity.status(OK).build();
    }

    @Operation(summary = "Change email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned user data"),
            @ApiResponse(responseCode = "401", description = "Invalid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnauthorizedAccessException.class)))
    })
    @PatchMapping("/change-email")
    public ResponseEntity<UserResponseDto> changeAccountEmail() {
        return ResponseEntity.status(OK).body(authorizationService.findAuthenticatedUserWithoutPassword());
    }

}
