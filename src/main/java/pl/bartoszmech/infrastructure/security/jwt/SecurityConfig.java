    package pl.bartoszmech.infrastructure.security.jwt;

    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.AllArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.web.AuthenticationEntryPoint;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    import java.io.IOException;

    import static org.springframework.http.HttpMethod.GET;
    import static org.springframework.http.HttpMethod.PATCH;
    import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
    import static pl.bartoszmech.domain.user.UserRoles.ADMIN;
    import static pl.bartoszmech.domain.user.UserRoles.EMPLOYEE;
    import static pl.bartoszmech.domain.user.UserRoles.MANAGER;

    @Configuration
    @AllArgsConstructor
    public class SecurityConfig {


        private static final String[] WHITE_LIST_URL = {
                "/accounts/token/**",
                "/accounts/register/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/webjars/**",
                "/v3/api-docs/**"
        };

        private final JwtAuthTokenFilter jwtAuthTokenFilter;

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public AccessDeniedHandler accessDeniedHandler() {
            return new AccessDeniedHandler();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .cors(AbstractHttpConfigurer::disable)
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeRequests(
                            auth -> auth
                                    .requestMatchers(WHITE_LIST_URL).permitAll()
                                    .requestMatchers(PATCH,"/api/tasks/{id}/complete").hasAnyAuthority(EMPLOYEE.getRoleName())
                                    .requestMatchers(GET,"/api/tasks/employee/{id}").hasAnyAuthority(ADMIN.getRoleName(), MANAGER.getRoleName(), EMPLOYEE.getRoleName())
                                    .requestMatchers(GET,"/api/tasks/{id}").hasAnyAuthority(ADMIN.getRoleName(), MANAGER.getRoleName(), EMPLOYEE.getRoleName())
                                    .requestMatchers("/api/tasks/**").hasAnyAuthority(ADMIN.getRoleName(), MANAGER.getRoleName())
                                    .requestMatchers("/api/users/stats/sorted-by-completed-tasks").hasAnyAuthority(ADMIN.getRoleName(), MANAGER.getRoleName())
                                    .requestMatchers("/api/users/**").hasAuthority(ADMIN.getRoleName())
                                    .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                    .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling(e -> e.authenticationEntryPoint(accessDeniedHandler()));
            return httpSecurity.build();
        }

    }
