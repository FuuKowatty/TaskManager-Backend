package pl.bartoszmech.infrastructure.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.domain.accountidentifier.AccountIdentifierFacade;
import pl.bartoszmech.domain.accountidentifier.dto.CreateUserRequestDto;
import pl.bartoszmech.domain.accountidentifier.dto.UserDto;
import pl.bartoszmech.infrastructure.auth.dto.JwtResponseDto;
import pl.bartoszmech.infrastructure.auth.dto.TokenRequestDto;
import pl.bartoszmech.infrastructure.auth.dto.TokenResponseDto;
import pl.bartoszmech.infrastructure.security.jwt.JwtAuthenticatorFacade;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static pl.bartoszmech.domain.accountidentifier.UserRoles.ADMIN;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AuthController {
    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;
    private final AccountIdentifierFacade accountIdentifierFacade;
    PasswordEncoder passwordEncoder;
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> authenticateAndGenerateToken(@RequestBody TokenRequestDto tokenRequestDto) {
        final JwtResponseDto jwtDto = jwtAuthenticatorFacade.authenticateAndGenerateToken(tokenRequestDto);
        String email = jwtDto.username();
        return ResponseEntity.status(OK).body(TokenResponseDto.builder()
                .token(jwtDto.token())
                .email(email)
                .id(accountIdentifierFacade.findByEmail(email).id())
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerAdmin(@RequestBody CreateUserRequestDto user) {
        return ResponseEntity.status(CREATED).body(accountIdentifierFacade.registerAdmin(CreateUserRequestDto.builder()
                .firstName(user.firstName())
                .lastName(user.lastName())
                .email(user.email())
                .password(passwordEncoder.encode(user.password()))
                .role(ADMIN)
                .build()));
    }
}
