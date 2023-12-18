package pl.bartoszmech.domain.accountidentifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {
    @Bean
    public AccountIdentifierFacade createUserFacade(UserService userService) {
        return new AccountIdentifierFacade(userService);
    }
}
