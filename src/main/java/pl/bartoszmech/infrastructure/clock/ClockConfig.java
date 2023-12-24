package pl.bartoszmech.infrastructure.clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfig {
    @Bean
    public Clock systemDefaultZoneClock() {
        return Clock.systemDefaultZone();
    }
}
