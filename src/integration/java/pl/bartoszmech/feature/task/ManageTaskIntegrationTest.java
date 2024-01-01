package pl.bartoszmech.feature.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.bartoszmech.IntegrationTest;
import pl.bartoszmech.application.request.CreateAndUpdateTaskRequestDto;
import pl.bartoszmech.application.response.TaskResponseDto;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
public class ManageTaskIntegrationTest {
    public static final int TASK_ID = 1;
    public static final long EMPLOYEE_ID = 3;
    @Autowired
    public Clock clock;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Test
    @WithMockUser(authorities = "admin")
    public void should_be_able_to_manage_tasks_as_admin() throws Exception {
        //Step 1: Admin can create a new task.
        Long createdTaskId = objectMapper.readValue(mockMvc.perform(post("/api/tasks")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateAndUpdateTaskRequestDto.builder()
                                .title("created title by admin")
                                .description("created description by admin")
                                .endDate(LocalDateTime.now().plusDays(1))
                                .assignedTo(3L)
                                .build())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(), TaskResponseDto.class).id();

        //Step 2: Admin can view a list of tasks.
        mockMvc.perform(get("/api/tasks")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        //Step 3: Admin cannot mark a task as completed.
        mockMvc.perform(patch("/api/tasks/" + createdTaskId + "/complete")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());


        //Step 4: Admin can update an existing task.
        mockMvc.perform(put("/api/tasks/" + createdTaskId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateAndUpdateTaskRequestDto.builder()
                                .title("edited title by admin")
                                .description("I was updated by admin")
                                .endDate(LocalDateTime.now(clock).plusDays(2))
                                .assignedTo(EMPLOYEE_ID)
                                .build())))
                .andExpect(status().isOk());

        //Step 5: Admin can delete an existing task
        mockMvc.perform(delete("/api/tasks/" + createdTaskId)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "manager")
    public void should_be_able_to_manage_tasks_as_manager() throws Exception {
        //Step 1: Manager can create a new task.
        Long createdTaskId = objectMapper.readValue(mockMvc.perform(post("/api/tasks")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateAndUpdateTaskRequestDto.builder()
                                .title("created title by manager")
                                .description("created description by manager")
                                .endDate(LocalDateTime.now().plusDays(1))
                                .assignedTo(3L)
                                .build())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(), TaskResponseDto.class).id();

        //Step 2: Manager can view a list of tasks.
        mockMvc.perform(get("/api/tasks")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        //Step 3: Manager cannot mark a task as completed.
        mockMvc.perform(patch("/api/tasks/" + createdTaskId + "/complete")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());


        //Step 4: Manager can update an existing task.
        mockMvc.perform(put("/api/tasks/" + createdTaskId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateAndUpdateTaskRequestDto.builder()
                                .title("edited title by manager")
                                .description("I was updated by XXXXXXX")
                                .endDate(LocalDateTime.now(clock).plusDays(2))
                                .assignedTo(EMPLOYEE_ID)
                                .build())))
                .andExpect(status().isOk());

        //Step 5: Manager can delete an existing task
        mockMvc.perform(delete("/api/tasks/" + createdTaskId)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(authorities = "employee")
    public void should_not_be_able_to_manage_tasks_as_employee() throws Exception {
        //Step 1 employee cant mark his task as completed
        mockMvc.perform(patch("/api/tasks/" + TASK_ID + "/complete")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());

        //Step 2 employee cant update task
        mockMvc.perform(put("/api/tasks/" + TASK_ID)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateAndUpdateTaskRequestDto.builder()
                                .title("edited title by employee")
                                .description("I was updated by XXXXXXXX")
                                .endDate(LocalDateTime.now(clock).plusDays(2))
                                .assignedTo(EMPLOYEE_ID)
                                .build())))
                .andExpect(status().isForbidden());

        //Step 3 employee cant delete task
        mockMvc.perform(delete("/api/tasks/" + 1)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());

        //Step 4 employee cant create task
        mockMvc.perform(post("/api/tasks")
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }
}
