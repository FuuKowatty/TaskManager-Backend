package pl.bartoszmech.feature.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.bartoszmech.IntegrationTest;
import pl.bartoszmech.application.request.CreateAndUpdateUserRequestDto;
import pl.bartoszmech.application.request.TokenRequestDto;
import pl.bartoszmech.application.response.TokenResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.bartoszmech.domain.user.UserRoles.ADMIN;

@IntegrationTest
@AutoConfigureMockMvc
public class ShouldRegisterAdminAndGenerateTokenIntegrationTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    private static final String JWT_PATTERN = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";;
    @Test
    public void shouldRegisterAdminAndGenerateToken() throws Exception {
        //given&when
        String adminEmail = "admin@gmail.com";
        String adminPassword = "zaq1@WSX";
        mockMvc.perform(post("/accounts/register")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(CreateAndUpdateUserRequestDto.builder()
                                .firstName("Dany")
                                .lastName("Abramov")
                                .email(adminEmail)
                                .password(adminPassword)
                                .role(ADMIN)
                                .build())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        TokenResponseDto admin = objectMapper.readValue(mockMvc.perform(post("/accounts/token")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TokenRequestDto.builder()
                                .username(adminEmail)
                                .password(adminPassword)
                                .build())))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), TokenResponseDto.class);

        //then
        assertThat(admin.token()).matches(JWT_PATTERN);
        assertThat(admin.email()).isEqualTo(adminEmail);
    }
}
