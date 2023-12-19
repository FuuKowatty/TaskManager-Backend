package pl.bartoszmech.domain.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {
    @Bean
    public UserFacade createUserFacade(UserService userService) {
        return new UserFacade(userService);
    }
}
