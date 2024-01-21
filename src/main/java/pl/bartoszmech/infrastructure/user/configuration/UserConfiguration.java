package pl.bartoszmech.infrastructure.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.bartoszmech.domain.user.repository.UserRepository;
import pl.bartoszmech.domain.user.service.UserService;
import pl.bartoszmech.domain.user.service.UserServiceImpl;

@Configuration
public class UserConfiguration {

    @Bean
    public UserService createUserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        return new UserServiceImpl(repository, passwordEncoder);
    }

}
