package pl.bartoszmech.infrastructure.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.bartoszmech.domain.user.repository.UserRepository;
import pl.bartoszmech.domain.user.service.UserService;
import pl.bartoszmech.domain.user.service.UserServiceImpl;

@Configuration
public class UserConfiguration {
    @Bean
    public UserService createUserFacade(UserRepository repository) {
        return new UserServiceImpl(repository);
    }
}
