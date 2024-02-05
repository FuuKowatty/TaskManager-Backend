package pl.bartoszmech.feature.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.bartoszmech.IntegrationTest;
import pl.bartoszmech.application.request.CreateUserDto;
import pl.bartoszmech.application.response.TokenResponseDto;
import pl.bartoszmech.application.response.UserResponseDto;
import pl.bartoszmech.domain.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.bartoszmech.domain.user.UserRoles.MANAGER;

@IntegrationTest
@AutoConfigureMockMvc
public class ManageUserIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void should_be_able_to_take_their_credentials() throws Exception {
        //Step: 0 login
        MvcResult loginAdminResponse = mockMvc.perform(post("/accounts/token")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .email("admin@example.com")
                                .password("123456")
                                .build())))
                .andExpect(status().isOk())
                .andReturn();
        String adminToken = objectMapper.readValue(loginAdminResponse.getResponse().getContentAsString(), TokenResponseDto.class).token();

        MvcResult loginEmployeeResponse = mockMvc.perform(post("/accounts/token")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .email("PeterJones@example.com")
                                .password("123456")
                                .build())))
                .andExpect(status().isOk())
                .andReturn();
        String employeeToken = objectMapper.readValue(loginEmployeeResponse.getResponse().getContentAsString(), TokenResponseDto.class).token();

        //Step 1: admin should be able to get their credentials
        MvcResult adminDataRes = mockMvc.perform(get("/accounts/data")
                        .contentType(APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andReturn();
        UserResponseDto adminData = objectMapper.readValue(adminDataRes.getResponse().getContentAsString(), UserResponseDto.class);
        assertThat(adminData.id()).isNotNull();
        assertThat(adminData.email()).isEqualTo("admin@example.com");

        //Step 2: employee should be able to get their credentials
        MvcResult employeeDataRes = mockMvc.perform(get("/accounts/data")
                        .contentType(APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + employeeToken))
                .andExpect(status().isOk())
                .andReturn();
        UserResponseDto employeeData = objectMapper.readValue(employeeDataRes.getResponse().getContentAsString(), UserResponseDto.class);
        assertThat(employeeData.id()).isNotNull();
        assertThat(employeeData.email()).isEqualTo("PeterJones@example.com");

    }

    @Test
    @WithMockUser(authorities = {"admin"})
    public void should_be_able_to_manage_users_as_an_admin() throws Exception {
        //Step 1: Admin can create user
        Long createdUserId = objectMapper.readValue(mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateUserDto.builder()
                                .firstName("Dany")
                                .lastName("Abramov")
                                .email("r23d33t3@gmail.com")
                                .password("zaq1@WSX")
                                .role(MANAGER)
                                .build())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(), UserResponseDto.class).id();


        //Step 2: Admin can get all users
        mockMvc.perform(get("/api/users")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        //Step 3: Admin can update user by id
        mockMvc.perform(put("/api/users/" + createdUserId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateUserDto.builder()
                                .firstName("Danny")
                                .lastName("Daniels")
                                .email("abc@gmail.com")
                                .password("password")
                                .role(MANAGER)
                                .build())))
                .andExpect(status().isOk());

        //Step 4: Admin can delete user by id
        mockMvc.perform(delete("/api/users/" + createdUserId)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(authorities = {"manager"})
    public void should_not_be_able_to_manage_users_as_a_manager() throws Exception {
        //Step 1: Manager cant create user
        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateUserDto.builder()
                                .firstName("Dany")
                                .lastName("Abramov")
                                .email("r23d33t3@gmail.com")
                                .password("zaq1@WSX")
                                .role(MANAGER)
                                .build())))
                .andExpect(status().isForbidden());

        //Step 2: Manager cant get any other user by id
        mockMvc.perform(get("/api/users/1")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());

        //Step 3: Manager cant get all users
        mockMvc.perform(get("/api/users")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());

        //Step 4: Manager cant update user by id
        mockMvc.perform(put("/api/users/1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateUserDto.builder()
                                .firstName("Danny")
                                .lastName("Daniels")
                                .email("manager@gmail.com")
                                .password("zaq1@WSX")
                                .role(MANAGER)
                                .build())))
                .andExpect(status().isForbidden());

        //Step 5: Manager cant delete user by id
        mockMvc.perform(delete("/api/users/1")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"employee"})
    public void should_not_be_able_to_manage_users_as_a_employee() throws Exception {
        //Step 1: Employee cant create user
        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateUserDto.builder()
                                .firstName("Dany")
                                .lastName("Abramov")
                                .email("r23d33t3@gmail.com")
                                .password("zaq1@WSX")
                                .role(MANAGER)
                                .build())))
                .andExpect(status().isForbidden());

        //Step 2: Employee cant get any other user by id
        mockMvc.perform(get("/api/users/1")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());

        //Step 3: Employee cant get all users
        mockMvc.perform(get("/api/users")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());

        //Step 4: Employee cant update user by id
        mockMvc.perform(put("/api/users/1")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateUserDto.builder()
                                .firstName("Danny")
                                .lastName("Daniels")
                                .email("manager@gmail.com")
                                .password("zaq1@WSX")
                                .role(MANAGER)
                                .build())))
                .andExpect(status().isForbidden());

        //Step 5: Employee cant delete user by id
        mockMvc.perform(delete("/api/users/1")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

}
