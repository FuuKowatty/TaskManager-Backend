package pl.bartoszmech.infrastructure.clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class ClockConfig {
    @Bean
    public Clock adjustableClock() {
        Instant initialInstant = Instant.now();
        ZoneId zone = ZoneId.systemDefault();
        return new AdjustableClock(initialInstant, zone);
    }
}
