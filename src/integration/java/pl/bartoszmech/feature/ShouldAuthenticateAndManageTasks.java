package pl.bartoszmech.feature;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;
import pl.bartoszmech.BaseIntegrationTest;
import pl.bartoszmech.domain.task.dto.CreateTaskRequestDto;
import pl.bartoszmech.domain.task.dto.TaskDto;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
public class ShouldAuthenticateAndManageTasks extends BaseIntegrationTest {
    @Test
    public void should_authenticate_and_manage_tasks() throws Exception {

//          Step 1: Send and receive "Unauthorized" for every endpoint due to the absence of tokens.

//          Step 2: Create an account.
//          Step 3: Log in.
//          Step 4: View a list of tasks (expecting an empty list).
//          Step 5: Create a task.
            CreateTaskRequestDto req = CreateTaskRequestDto.builder()
                    .title("Mdfiqwi")
                    .description("2ff2f 2f 2f")
                    .endDate(LocalDateTime.now(adjustableClock).plusDays(1))
                    .assignedTo(997L)
                    .build();
            //given&when
            MvcResult createdTaskResponse = mockMvc.perform(post("/api/tasks")
                            .content(objectMapper.writeValueAsString(req))
                            .contentType(APPLICATION_JSON_VALUE))
            //then
                    .andExpect(status().isCreated())
                    .andReturn();
            TaskDto createdTask = objectMapper.readValue(createdTaskResponse.getResponse().getContentAsString(), new TypeReference<>() {});

//          Step 6: View a list of tasks and receive created one as response.
            //given&when
            MvcResult taskListAfterGetUnauthorizedResponse = mockMvc.perform(get("/api/tasks")
                            .contentType(APPLICATION_JSON_VALUE))
            //then
                    .andExpect(status().isOk())
                    .andReturn();

            List<TaskDto> taskListAfterGetUnauthorized = objectMapper.readValue(taskListAfterGetUnauthorizedResponse.getResponse().getContentAsString(), new TypeReference<>() {});
            TaskDto createdTaskButFromList = taskListAfterGetUnauthorized.get(0);
            assertThat(taskListAfterGetUnauthorized.size()).isEqualTo(1);
            assertThat(createdTaskButFromList).isEqualTo(createdTask);
//        step 7  get created task by id
            MvcResult taskGeyByIdResponse = mockMvc.perform(get("/api/tasks/" + createdTask.id())
                            .contentType(APPLICATION_JSON_VALUE))
                    //then
                            .andExpect(status().isOk())
                            .andReturn();
            TaskDto taskGetById = objectMapper.readValue(taskGeyByIdResponse.getResponse().getContentAsString(), new TypeReference<>() {});
            assertThat(taskGetById).isEqualTo(createdTask);
//        step 8 DeleteTaskById
            MvcResult deletedTaskByIdResponse = mockMvc.perform(delete("/api/tasks/" + createdTask.id())
                            .contentType(APPLICATION_JSON_VALUE))
                    //then
                    .andExpect(status().isOk())
                    .andReturn();
            TaskDto deletedTaskById = objectMapper.readValue(deletedTaskByIdResponse.getResponse().getContentAsString(), new TypeReference<>() {});
            assertThat(deletedTaskById).isEqualTo(createdTask);
//        step 9 check if database is empty
            MvcResult taskListAfterDeleteTaskResponse = mockMvc.perform(get("/api/tasks")
                        .contentType(APPLICATION_JSON_VALUE))
                //then
                .andExpect(status().isOk())
                .andReturn();

            List<TaskDto> taskListAfterDeleteTask = objectMapper.readValue(taskListAfterDeleteTaskResponse.getResponse().getContentAsString(), new TypeReference<>() {});
            assertThat(taskListAfterDeleteTask).isEmpty();
//        Step 7: Test endpoints to validate filters.
//        Step 8: An employee attempts to create a user/task and receives an error.
//        Step 9: A manager attempts to create a user and receives an error.
//                Step 10: An employee displays only their tasks and receives a valid response.
//                Step 11: An employee attempts to display all tasks and receives an error.
//                Step 12: A manager tries to create a task and receives an "OK" response.
//                Step 13: A manager attempts to create a user and receives an error.
//                Step 14: A manager tries to delete a task and receives an "OK" response.
//                Step 15: A manager tries to edit a task and receives an "OK" response.
//                Step 16: Admin conducts CRUD operations on users.
//        Step 17: An employee tries to mark a task as completed and receives an "OK" response.
    }
}
