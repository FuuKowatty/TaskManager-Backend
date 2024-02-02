package pl.bartoszmech;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.PostgreSQLContainer;
import pl.bartoszmech.domain.task.Task;
import pl.bartoszmech.domain.task.repository.TaskRepository;
import pl.bartoszmech.domain.user.User;
import pl.bartoszmech.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;

import static pl.bartoszmech.domain.task.TaskStatus.*;
import static pl.bartoszmech.domain.user.UserRoles.*;
import static pl.bartoszmech.domain.user.UserRoles.EMPLOYEE;

@Component
class TestcontainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.1");

    static {
        postgres.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword()
        ).applyTo(ctx.getEnvironment());
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    public void onBootApp() {
        //ID is reserved 1-ADMIN, 2-MANAGER, (3-5)-EMPLOYEE
        Arrays.asList(
                new User(1L,"John" , "Doe", "admin@example.com", passwordEncoder.encode("123456"), ADMIN),
                new User(2L,"Jane" , "Doe", "JaneDoe@example.com", "123456", MANAGER),
                new User(3L,"Peter" , "Jones", "PeterJones@example.com", passwordEncoder.encode("123456"), EMPLOYEE),
                new User(4L,"Mary" , "Smith", "MarySmith@example.com", "123456", EMPLOYEE),
                new User(5L,"Michael" , "Brown", "MichaelBrown@example.com", "123456", EMPLOYEE)
        ).forEach(user -> userRepository.save(user));

        //DO NOT ASSIGN TASK TO ID 1 or 2!
        Arrays.asList(
                new Task(1L, "Create a new feature", "Develop and implement a new feature for our product", PENDING, LocalDateTime.now(), LocalDateTime.now().plusDays(7), null, 4L),
                new Task(2L, "Fix a bug in the payment system", "Identify and fix a bug in our payment system that is causing some users to experience issues", PENDING, LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(5), null, 5L),
                new Task(3L, "Prepare for the upcoming marketing campaign", "Create promotional materials, develop campaign strategies, and coordinate with marketing team", PENDING, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(10), null, 5L),
                new Task(4L, "Update user documentation", "Review and update our user documentation to reflect the latest changes to the product", COMPLETED, LocalDateTime.now().minusDays(2), LocalDateTime.now(), LocalDateTime.now().minusDays(1), 3L),
                new Task(5L, "Resolve customer support tickets", "Respond to customer inquiries, investigate issues, and provide solutions to resolve customer support tickets", FAILED, LocalDateTime.now().minusDays(4), LocalDateTime.now().plusDays(3), null, 4L)
        ).forEach(task -> taskRepository.save(task));
    }

}