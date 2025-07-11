package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.AuthRequest;
import eu.deltasource.elearning.DTOs.AuthResponse;
import eu.deltasource.elearning.DTOs.RegisterRequest;
import eu.deltasource.elearning.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidRegisterRequest_whenRegister_thenReturnAuthResponse() {
        // Given
        RegisterRequest request = new RegisterRequest();
        AuthResponse expectedResponse = new AuthResponse();
        when(authService.register(request)).thenReturn(expectedResponse);

        // When
        AuthResponse response = authController.register(request);

        // Then
        assertEquals(expectedResponse, response);
        verify(authService, times(1)).register(request);
    }

    @Test
    void givenValidAuthRequest_whenLogin_thenReturnAuthResponse() {
        // Given
        AuthRequest request = new AuthRequest();
        AuthResponse expectedResponse = new AuthResponse();
        when(authService.login(request)).thenReturn(expectedResponse);

        // When
        AuthResponse response = authController.login(request);

        // Then
        assertEquals(expectedResponse, response);
        verify(authService, times(1)).login(request);
    }

    @Test
    void givenValidRefreshToken_whenRefresh_thenReturnAuthResponse() {
        // Given
        String refreshToken = "valid-refresh-token";
        AuthResponse expectedResponse = new AuthResponse();
        when(authService.refreshToken(refreshToken)).thenReturn(expectedResponse);

        // When
        AuthResponse response = authController.refresh(refreshToken);

        // Then
        assertEquals(expectedResponse, response);
        verify(authService, times(1)).refreshToken(refreshToken);
    }

    @Test
    void givenValidUpdateProfileRequest_whenUpdateProfile_thenReturnAuthResponse() {
        // Given
        RegisterRequest request = new RegisterRequest();
        AuthResponse expectedResponse = new AuthResponse();
        when(authService.updateProfile(request)).thenReturn(expectedResponse);

        // When
        AuthResponse response = authController.updateProfile(request);

        // Then
        assertEquals(expectedResponse, response);
        verify(authService, times(1)).updateProfile(request);
    }

    @Test
    void givenValidAccessToken_whenLogout_thenVerifyServiceCalled() {
        // Given
        String accessToken = "valid-access-token";

        // When
        authController.logout(accessToken);

        // Then
        verify(authService, times(1)).logout(accessToken);
    }
}
