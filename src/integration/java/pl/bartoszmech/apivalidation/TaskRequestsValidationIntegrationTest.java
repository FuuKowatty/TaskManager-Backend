package pl.bartoszmech.apivalidation;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.bartoszmech.BaseIntegrationTest;
import pl.bartoszmech.infrastructure.apivalidation.ValidationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskRequestsValidationIntegrationTest extends BaseIntegrationTest {
    @Test
    @WithMockUser(authorities = "admin")
    public void should_return_bad_request_and_message_when_provide_invalid_task_credentials() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .content("""
                                {
                                    "title": "do the dishes",
                                    "description": "bla bla bla",
                                    "assignedTO": 1,
                                    "endDate": "12 May 2003 15:00"
                                }
                                """.trim())
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        String responseWhenPassingEmptyObject = mockMvc.perform(post("/api/tasks")
                        .content("""
                                {
                                }
                                """.trim())
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ValidationResponse createTaskValidationMessages = objectMapper.readValue(responseWhenPassingEmptyObject, ValidationResponse.class);
        assertThat(createTaskValidationMessages.messages()).containsExactlyInAnyOrder(
                "Task title must not be blank.",
                "Task title is required.",
                "Task description is required.",
                "Task description must not be blank.",
                "Task end date is required.",
                "Task assigned to is required."
        );
    }
}
