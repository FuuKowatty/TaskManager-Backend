package pl.bartoszmech.infrastructure.auth.userdetails;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.bartoszmech.domain.accountidentifier.AccountIdentifierFacade;

@Configuration
public class UserDetailsConfig {
    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService(AccountIdentifierFacade loginAndRegisterFacade) {
        return new UserDetailsService(loginAndRegisterFacade);
    }
}
