package pl.bartoszmech.infrastructure.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.infrastructure.auth.dto.JwtResponseDto;
import pl.bartoszmech.infrastructure.auth.dto.TokenRequestDto;
import pl.bartoszmech.infrastructure.security.jwt.JwtAuthenticatorFacade;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AuthController {
    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;
    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@RequestBody TokenRequestDto tokenRequestDto) {
        final JwtResponseDto jwtResponseDto = jwtAuthenticatorFacade.authenticateAndGenerateToken(tokenRequestDto);
        return ResponseEntity.status(OK).body(jwtResponseDto);
    }
}
