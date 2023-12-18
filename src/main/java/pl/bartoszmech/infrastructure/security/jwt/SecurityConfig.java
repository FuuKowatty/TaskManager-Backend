    package pl.bartoszmech.infrastructure.security.jwt;

    import lombok.AllArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
    import static pl.bartoszmech.domain.accountidentifier.UserRoles.ADMIN;
    import static pl.bartoszmech.domain.accountidentifier.UserRoles.MANAGER;

    @Configuration
    @AllArgsConstructor
    public class SecurityConfig {

        private final JwtAuthTokenFilter jwtAuthTokenFilter;

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .csrf(csrf -> csrf.disable())
                    .authorizeRequests(
                            auth -> auth
                                    .requestMatchers("/swagger-ui/**").permitAll()
                                    .requestMatchers("/accounts/token/**").permitAll()
                                    .requestMatchers("/accounts/register/**").permitAll()
                                    .requestMatchers("/swagger-resources/**").permitAll()
                                    .requestMatchers("api/tasks").hasAnyAuthority(ADMIN.getRoleName(), MANAGER.getRoleName())
                                    .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                    .headers(header -> header.frameOptions(op -> op.disable()))
                    .httpBasic(httpBasic -> httpBasic.disable())
                    .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
            return httpSecurity.build();
        }

    }
