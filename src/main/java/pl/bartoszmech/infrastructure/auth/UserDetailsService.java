package pl.bartoszmech.infrastructure.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import pl.bartoszmech.domain.accountidentifier.AccountIdentifierFacade;
import pl.bartoszmech.domain.accountidentifier.dto.UserDto;

import java.util.Collections;

@AllArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AccountIdentifierFacade accountIdentifierFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto userDto = accountIdentifierFacade.findByEmail(username);
        return getUser(userDto);
    }

    private org.springframework.security.core.userdetails.User getUser(UserDto user) {
        return new org.springframework.security.core.userdetails.User(
                user.email(),
                user.password(),
                Collections.emptyList());
    }
}
