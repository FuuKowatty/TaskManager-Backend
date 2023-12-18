package pl.bartoszmech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.bartoszmech.infrastructure.security.jwt.JwtConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = JwtConfigurationProperties.class)
public class TaskManager {
    public static void main(String[] args) {
        SpringApplication.run(TaskManager.class, args);
    }
}