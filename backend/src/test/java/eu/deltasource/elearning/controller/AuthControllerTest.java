package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.AuthRequest;
import eu.deltasource.elearning.DTOs.AuthResponse;
import eu.deltasource.elearning.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidAuthRequest_whenLogin_thenReturnsAuthResponse() throws Exception {
        // Given
        AuthRequest request = new AuthRequest();
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        AuthResponse response = new AuthResponse();
        when(authService.login(any(AuthRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService, times(1)).login(any(AuthRequest.class));
    }

    @Test
    void givenRefreshToken_whenRefresh_thenReturnsAuthResponse() throws Exception {
        // Given
        String refreshToken = "Bearer some-refresh-token";
        AuthResponse response = new AuthResponse();
        when(authService.refreshToken(any(String.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", refreshToken))
                .andExpect(status().isOk());
        verify(authService, times(1)).refreshToken(eq(refreshToken));
    }

    @Test
    void givenAccessToken_whenLogout_thenReturnsOk() throws Exception {
        // Given
        String accessToken = "Bearer some-access-token";

        // When & Then
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());
        verify(authService, times(1)).logout(eq(accessToken));
    }
}
