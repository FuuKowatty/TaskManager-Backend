package pl.bartoszmech.apivalidation;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.bartoszmech.BaseIntegrationTest;
import pl.bartoszmech.infrastructure.apivalidation.ValidationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRequestValidation extends BaseIntegrationTest {
    @Test
    @WithMockUser(authorities = "admin")
    public void should_return_bad_request_and_message_when_provide_invalid_user_credentials() throws Exception {
        mockMvc.perform(post("/api/users")
                        .content("""
                                {
                                  "firstName": "Bartosz",
                                  "lastName": "Mech",
                                  "email": "bartoszmech0@gmail.com",
                                  "password": "zaq1@WSX",
                                  "role": "invalidRole"
                                }
                                """.trim())
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        String responseWhenPassingEmptyValues = mockMvc.perform(post("/api/users")
                        .content("""
                                {
                                    "firstName": "",
                                    "lastName": "",
                                    "email": "",
                                    "password": "",
                                    "role": "employee"
                                }
                                """.trim())
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseWhenPassingEmptyValues);
        ValidationResponse createUsersValidationResponse = objectMapper.readValue(responseWhenPassingEmptyValues, ValidationResponse.class);
        assertThat(createUsersValidationResponse.messages()).containsExactlyInAnyOrder(
        "First name must not be blank.",
                "Last name must be alphanumeric.",
                "Email must not be blank.",
                "Password must be at least 6 characters long.",
                "First name must be at least 2 characters long.",
                "Email must be a valid email address.",
                "First name must be alphanumeric.",
                "Last name must be at least 2 characters long.",
                "Password must not be blank.",
                "Last name must not be blank."
        );
    }
}
