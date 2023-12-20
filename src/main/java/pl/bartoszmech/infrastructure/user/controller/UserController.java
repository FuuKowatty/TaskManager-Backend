package pl.bartoszmech.infrastructure.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.bartoszmech.domain.user.UserFacade;
import pl.bartoszmech.domain.user.dto.CreateUserRequestDto;
import pl.bartoszmech.domain.user.dto.UpdateUserRequestDto;
import pl.bartoszmech.domain.user.dto.UserDto;
import pl.bartoszmech.infrastructure.auth.AuthorizationService;
import pl.bartoszmech.infrastructure.user.EmployeeStatisticDto;
import pl.bartoszmech.infrastructure.user.EmployeeStatisticService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    UserFacade userFacade;
    AuthorizationService authorizationService;
    PasswordEncoder passwordEncoder;
    EmployeeStatisticService employeeStatisticService;
    @GetMapping
    public ResponseEntity<List<UserDto>> list() {
        return ResponseEntity.status(OK).body(userFacade.listUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(userFacade.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody CreateUserRequestDto requestDto) {
        authorizationService.checkIfUserWantsCreateAdmin(requestDto.role());
        return ResponseEntity.status(CREATED).body(userFacade.createUser(CreateUserRequestDto.builder()
                .firstName(requestDto.firstName())
                .lastName(requestDto.lastName())
                .email(requestDto.email())
                .password(passwordEncoder.encode(requestDto.password()))
                .role(requestDto.role())
                .build()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteById(@PathVariable("id") long id) {
        return ResponseEntity.status(OK).body(userFacade.deleteById(id));    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> editUserById(@PathVariable("id") long id, @RequestBody UpdateUserRequestDto requestDto) {
        authorizationService.checkIfUserWantsCreateAdmin(requestDto.role());
        return ResponseEntity.status(OK).body(userFacade.updateUser(id, requestDto));    }

    @GetMapping("/stats/sorted-by-completed-tasks")
    public List<EmployeeStatisticDto> listBestEmployee(@RequestParam(
            name = "last-months",
            required = false,
            defaultValue = "6"
    ) int lastMonths) {
        return employeeStatisticService.sortEmployeesByCompletedTasks(lastMonths);
    }
}

