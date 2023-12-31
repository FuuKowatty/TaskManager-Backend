package pl.bartoszmech.domain.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.domain.user.dto.UserDto;
import pl.bartoszmech.domain.user.service.UserService;
import pl.bartoszmech.domain.user.service.UserServiceImpl;
import pl.bartoszmech.infrastructure.apivalidation.ResourceNotFound;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.bartoszmech.domain.user.UserRoles.ADMIN;
import static pl.bartoszmech.domain.user.UserRoles.EMPLOYEE;
import static pl.bartoszmech.domain.user.UserRoles.MANAGER;

public class UserFacadeTest {
    UserService userService = new UserServiceImpl(new UserRepositoryTestImpl());
    @Test
    public void should_successfully_create_user() {
        //given
        String firstName = "Dany";
        String lastName = "Abramov";
        String email = "example@gmail.com";
        String password = "zaq1@WSX";
        UserRoles role = EMPLOYEE;
        //when
        UserResponseDto savedUser = userService.createUser(CreateAndUpdateUserRequestDto
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .build()
        );
        //then
        assertAll("Create User assertions",
                () -> assertThat(savedUser.firstName()).isEqualTo(firstName),
                () -> assertThat(savedUser.lastName()).isEqualTo(lastName),
                () -> assertThat(savedUser.email()).isEqualTo(email),
                () -> assertThat(savedUser.role()).isEqualTo(role),
                () -> assertThat(savedUser.id()).isNotNull()
        );
    }

    @Test
    public void should_throw_exception_if_email_is_already_used() {
        //given
        String email = "example@gmail.com";
        userService.createUser(CreateAndUpdateUserRequestDto
                        .builder()
                        .firstName("Dany")
                        .lastName("Abramov")
                        .email(email)
                        .password("zaq1@WSX")
                        .role(EMPLOYEE)
                        .build());
        //when
        Throwable emailTaken = assertThrows(
                EmailTakenException.class,
                () -> userService.createUser(CreateAndUpdateUserRequestDto.builder()
                        .firstName("rifsif")
                        .lastName("KMduiroqr")
                        .email(email)
                        .password("123QWE@#!")
                        .role(MANAGER)
                        .build())
        );
        //then
        assertThat(emailTaken).isInstanceOf(EmailTakenException.class);
        assertThat(emailTaken.getMessage()).isEqualTo("User email is taken");
    }

    @Test
    public void should_success_return_empty_list_after_list_users() {
        //when
        List<UserResponseDto> users = userService.listUsers();
        //then
        assertThat(users).isEmpty();
    }

    @Test
    public void should_success_return_list_of_added_users() {
        //given
        String firstName = "Dany";
        String lastName = "Abramov";
        String email = "example@gmail.com";
        String password = "zaq1@WSX";
        UserRoles role = EMPLOYEE;
        UserResponseDto savedUser = userService.createUser(CreateAndUpdateUserRequestDto
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .build()
        );
        //when
        List<UserResponseDto> users = userService.listUsers();
        //then
        assertThat(users.get(0)).isEqualTo(savedUser);
    }

    @Test
    public void should_find_user_by_id() {
        //given
        String firstName = "Dany";
        String lastName = "Abramov";
        String email = "example@gmail.com";
        String password = "zaq1@WSX";
        UserRoles role = EMPLOYEE;
        UserResponseDto savedUser = userService.createUser(CreateAndUpdateUserRequestDto
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .build()
        );
        //when
        UserResponseDto foundUser = userService.findById(savedUser.id());
        //then
        assertThat(foundUser).isEqualTo(savedUser);
    }

    @Test
    public void should_throw_exception_when_provided_invalid_id_in_findById() {
        //given
        Long id = 997L;
        //when
        Throwable userNotFound = assertThrows(ResourceNotFound.class, () -> userService.findById(id));
        //then
        assertThat(userNotFound).isInstanceOf(ResourceNotFound.class);
        assertThat(userNotFound.getMessage()).isEqualTo("User with provided id could not be found");
    }

    @Test
    public void should_success_delete_user_by_id() {
        //given
        String firstName = "Dany";
        String lastName = "Abramov";
        String email = "example@gmail.com";
        String password = "zaq1@WSX";
        UserRoles role = EMPLOYEE;
        UserResponseDto savedUser = userService.createUser(CreateAndUpdateUserRequestDto
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .build()
        );
        //when
        UserResponseDto deletedUser = userService.deleteById(savedUser.id());
        //then
        assertThat(deletedUser).isEqualTo(savedUser);
        assertThat(userService.listUsers()).isEmpty();
    }

    @Test
    public void should_throw_not_found_exception_when_client_provide_invalid_id_in_deleteById() {
        //given
        long id = 997L;
        //when
        Throwable userNotFound = assertThrows(ResourceNotFound.class, () -> userService.deleteById(id));
        //then
        assertThat(userNotFound).isInstanceOf(ResourceNotFound.class);
        assertThat(userNotFound.getMessage()).isEqualTo("User with provided id could not be found");
    }

    @Test
    public void should_success_update_user() {
        //given
        String firstName = "Dany";
        String lastName = "Abramov";
        String email = "example@gmail.com";
        String password = "zaq1@WSX";
        UserRoles role = EMPLOYEE;
        UserResponseDto savedUser = userService.createUser(CreateAndUpdateUserRequestDto
                .builder()
                .firstName("OtherNameThanDany")
                .lastName("OtherSurnameThanDany")
                .email("qwe123@qwe123.pl")
                .password(password)
                .role(role)
                .build()
        );
        //when
        UserResponseDto updatedUser = userService.updateUser(savedUser.id(), CreateAndUpdateUserRequestDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .build());
        //then
        assertAll("Update user assertions",
                () -> assertThat(updatedUser.firstName()).isEqualTo(firstName),
                () -> assertThat(updatedUser.lastName()).isEqualTo(lastName),
                () -> assertThat(updatedUser.email()).isEqualTo(email),
                () -> assertThat(updatedUser.role()).isEqualTo(savedUser.role()),
                () -> assertThat(updatedUser.id()).isNotNull(),
                () -> assertThat(updatedUser.id()).isEqualTo(savedUser.id())
        );
    }

    @Test
    public void should_throw_exception_if_client_provide_invalid_id_in_updateUser() {
        //given
        long id = 997L;
        String password = "zaq1@WSX";
        UserRoles role = EMPLOYEE;
        //when
        Throwable taskNotFound = assertThrows(ResourceNotFound.class, () -> userService.updateUser(id, CreateAndUpdateUserRequestDto.builder()
                .firstName("OtherNameThanDany")
                .lastName("OtherSurnameThanDany")
                .email("qwe123@qwe123.pl")
                .password(password)
                .role(role)
                .build()));
        //then
        assertThat(taskNotFound).isInstanceOf(ResourceNotFound.class);
        assertThat(taskNotFound.getMessage()).isEqualTo("User with provided id could not be found");
    }

    @Test
    public void should_throw_exception_if_email_is_taken_while_updating_user() {
        //given
        String firstName = "Dany";
        String lastName = "Abramov";
        String email = "example@gmail.com";
        String password = "zaq1@WSX";
        UserRoles role = EMPLOYEE;
        userService.createUser(CreateAndUpdateUserRequestDto
                .builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .role(role)
                .build()
        );
        UserResponseDto savedUser2 = userService.createUser(CreateAndUpdateUserRequestDto
                .builder()
                .firstName("Bartosz")
                .lastName("Mech")
                .email("randomEmail@wp.pl")
                .password(password)
                .role(role)
                .build()
        );
        //when
        Throwable emailTaken = assertThrows(
                EmailTakenException.class,
                () -> userService.updateUser(savedUser2.id(), CreateAndUpdateUserRequestDto.builder()
                        .firstName("rifsif")
                        .lastName("KMduiroqr")
                        .email(email)
                        .password("123QWE@#!")
                        .role(MANAGER)
                        .build())
        );
        //then
        assertThat(emailTaken).isInstanceOf(EmailTakenException.class);
        assertThat(emailTaken.getMessage()).isEqualTo("User email is taken");

    }

    @Test
    public void should_find_user_by_email() {
        //given
        String email = "example@gmail.com";
        String password = "XXXXXXXX";
        UserResponseDto savedUser = userService.createUser(CreateAndUpdateUserRequestDto
                .builder()
                .firstName("Dany")
                .lastName("Abramov")
                .email(email)
                .password(password)
                .role(EMPLOYEE)
                .build()
        );
        //when
        UserDto foundUser = userService.findByEmail(email);
        //then
        assertAll("User credentials test",
                () -> assertThat(foundUser.id()).isNotNull(),
                () -> assertThat(foundUser.email()).isEqualTo(savedUser.email()),
                () -> assertThat(foundUser.firstName()).isEqualTo(savedUser.firstName()),
                () -> assertThat(foundUser.lastName()).isEqualTo(savedUser.lastName()),
                () -> assertThat(foundUser.role()).isEqualTo(savedUser.role())
        );
    }

    @Test
    public void should_throw_exception_if_client_provide_non_existing_email_in_findByEmail() {
        //given
        String nonExistingEmail = "example@gmail.com";
        //when
        Throwable userNotFound = assertThrows(BadCredentialsException.class, () -> userService.findByEmail(nonExistingEmail));assertThrows(BadCredentialsException.class, () -> userService.findByEmail(nonExistingEmail));
        assertThat(userNotFound.getMessage()).isEqualTo("User with provided email could not be found");
    }
    @Test
    public void should_create_user_with_admin_role_without_passing_any_role() {
        //given
        String email = "example@gmail.com";
        //when
        UserResponseDto savedUser = userService.registerAdmin(CreateAndUpdateUserRequestDto.builder()
                .firstName("Dany")
                .lastName("Abramov")
                .email(email)
                .password("XXXXXXXX")
                .build()
        );
        //then
        assertThat(savedUser.role()).isEqualTo(ADMIN);
    }

    @Test
    public void should_create_user_with_admin_role_passing_other_role() {
        //given
        String email = "example@gmail.com";
        //when
        UserResponseDto savedUser = userService.registerAdmin(CreateAndUpdateUserRequestDto.builder()
                .firstName("Dany")
                .lastName("Abramov")
                .email(email)
                .password("XXXXXXXX")
                .role(EMPLOYEE)
                .build()
        );
        //then
        assertThat(savedUser.role()).isEqualTo(ADMIN);
    }

    @Test
    public void should_throw_email_is_existing_in_registerAdmin() {
        //given
        String email = "example@gmail.com";
        userService.createUser(CreateAndUpdateUserRequestDto.builder()
                .firstName("Dany")
                .lastName("Abramov")
                .email(email)
                .password("XXXXXXXX")
                .role(EMPLOYEE)
                .build());
        //when
        Throwable emailTaken = assertThrows(
                EmailTakenException.class,
                () -> userService.registerAdmin((CreateAndUpdateUserRequestDto.builder()
                        .firstName("Dany")
                        .lastName("Abramov")
                        .email(email)
                        .password("XXXXXXXX")
                        .build()
                ))
        );
        //then
        assertThat(emailTaken.getMessage()).isEqualTo("User email is taken");
    }
}
