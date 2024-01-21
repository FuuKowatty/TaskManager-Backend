package pl.bartoszmech.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class PasswordEncoderTestImpl implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
