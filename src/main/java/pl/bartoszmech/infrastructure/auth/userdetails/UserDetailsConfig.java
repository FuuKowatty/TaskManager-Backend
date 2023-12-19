package pl.bartoszmech.infrastructure.auth.userdetails;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.bartoszmech.domain.user.UserFacade;

@Configuration
public class UserDetailsConfig {
    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService(UserFacade loginAndRegisterFacade) {
        return new UserDetailsService(loginAndRegisterFacade);
    }
}
