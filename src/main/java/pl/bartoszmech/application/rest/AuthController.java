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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.application.services.AuthorizationService;
import pl.bartoszmech.domain.user.UserMapper;
import pl.bartoszmech.domain.user.service.UserService;
import pl.bartoszmech.infrastructure.apivalidation.ValidationResponse;
import pl.bartoszmech.infrastructure.auth.dto.JwtResponseDto;
import pl.bartoszmech.application.request.TokenRequestDto;
import pl.bartoszmech.application.response.TokenResponseDto;
import pl.bartoszmech.infrastructure.security.jwt.JwtAuthenticatorService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AuthController {

    private final JwtAuthenticatorService jwtAuthenticatorService;
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
        final JwtResponseDto jwtDto = jwtAuthenticatorService.authenticateAndGenerateToken(tokenRequestDto);
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

    @GetMapping("/data")
    public ResponseEntity<UserResponseDto> getUserById() {
        return ResponseEntity.status(OK).body(authorizationService.findAuthenticatedUserWithoutPassword());
    }

}
