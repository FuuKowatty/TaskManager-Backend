package pl.bartoszmech.feature;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.bartoszmech.BaseIntegrationTest;
import pl.bartoszmech.domain.accountidentifier.dto.CreateUserRequestDto;

import pl.bartoszmech.infrastructure.auth.dto.JwtResponseDto;
import pl.bartoszmech.infrastructure.auth.dto.TokenRequestDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.bartoszmech.domain.accountidentifier.UserRoles.ADMIN;
import static pl.bartoszmech.domain.accountidentifier.UserRoles.EMPLOYEE;
import static pl.bartoszmech.domain.accountidentifier.UserRoles.MANAGER;

public class TaskIntegrationTest extends BaseIntegrationTest {
    @Test
    public void should_authenticate_and_manage_tasks_if_user_has_permission() throws Exception {
        //SECURITY
        //Step 1: An admin user can log in to their account.
        String adminEmail = "admin@gmail@gmail.com";
        String adminPassword = "zaq1@WSX";
        mockMvc.perform(post("/accounts/register")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateUserRequestDto.builder()
                                .firstName("Dany")
                                .lastName("Abramov")
                                .email(adminEmail)
                                .password(adminPassword)
                                .role(ADMIN)
                                .build())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String adminToken = objectMapper.readValue(mockMvc.perform(post("/accounts/token")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TokenRequestDto.builder()
                                .username(adminEmail)
                                .password(adminPassword)
                                .build())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), JwtResponseDto.class).token();
        //Step 2: A manager user can log in to their account.
        String managerEmail = "manager@gmail@gmail.com";
        String managerPassword = "zaq1@WSX";
        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateUserRequestDto.builder()
                                .firstName("Dany")
                                .lastName("Abramov")
                                .email(managerEmail)
                                .password(managerPassword)
                                .role(MANAGER)
                                .build())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String managerToken = objectMapper.readValue(mockMvc.perform(post("/accounts/token")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TokenRequestDto.builder()
                                .username(managerEmail)
                                .password(managerPassword)
                                .build())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), JwtResponseDto.class).token();
        //Step 3: An employee user can log in to their account.
        String employeeEmail = "employee@gmail@gmail.com";
        String employeePassword = "zaq1@WSX";
        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateUserRequestDto.builder()
                                .firstName("Dany")
                                .lastName("Abramov")
                                .email(employeeEmail)
                                .password(employeePassword)
                                .role(EMPLOYEE)
                                .build())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String employeeToken = objectMapper.readValue(mockMvc.perform(post("/accounts/token")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TokenRequestDto.builder()
                                .username(employeeEmail)
                                .password(employeePassword)
                                .build())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), JwtResponseDto.class).token();
        //Step 4: A user with incorrect email cannot log in.
        mockMvc.perform(post("/accounts/token")
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TokenRequestDto.builder()
                                .username("NonExistingEmail@gmail.com")
                                .password("zaq1@WSX")
                                .build())))
                .andExpect(status().isUnauthorized());
        //TASK DOMAIN
        //Step 5: Admin can create a new task.
        //Step 6: Manager can create a new task.
        //Step 7: Employee cannot create a new task.
        //Step 8: Admin can edit an existing task.
        //Step 9: Manager can edit an existing task.
        //Step 10: Employee cannot edit an existing task.
        //Step 11: Admin can delete an existing task.
        //Step 12: Manager can delete an existing task.
        //Step 13: Employee cannot delete an existing task.
        //Step 14: Admin can assign a task to a user.
        //Step 15: Manager can assign a task to a user.
        //Step 16: Employee cannot assign a task to a user.
        //Step 17: Admin can mark a task as completed.
        //Step 18: Manager cannot mark a task as completed.
        //Step 19: Employee mark task as completed.

        //USER DOMAIN
        //Step 21: Manager cannot add a new user.
        //Step 22: Employee cannot add a new user.
        //Step 23: Admin can edit an existing user.
        //Step 24: Manager cannot edit an existing user.
        //Step 25: Employee cannot edit an existing user.
        //Step 26: Admin can delete an existing user.
        //Step 27: Manager cannot delete an existing user.
        //Step 28: Employee cannot delete an existing user.
        //Step 29: Admin can read all users
        //Step 30: Manager cannot read all users
        //Step 31: Employee cannot read all user.
        //findById?
    }

}
