package pl.bartoszmech.infrastructure.auth.userdetails;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.bartoszmech.domain.user.dto.UserDto;
import pl.bartoszmech.domain.user.service.UserService;
import pl.bartoszmech.infrastructure.auth.error.InvalidPasswordException;

import java.util.List;

@AllArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws InvalidPasswordException {
        UserDto userDto = userService.findByEmail(username);
        return getUser(userDto);
    }

    private org.springframework.security.core.userdetails.User getUser(UserDto user) {
        return new org.springframework.security.core.userdetails.User(
                user.email(),
                user.password(),
                List.of(new SimpleGrantedAuthority(user.role().getRoleName())));
    }

}
