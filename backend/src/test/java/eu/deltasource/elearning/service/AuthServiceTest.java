package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.AuthRequest;
import eu.deltasource.elearning.DTOs.AuthResponse;
import eu.deltasource.elearning.DTOs.RegisterRequest;
import eu.deltasource.elearning.enums.Role;
import eu.deltasource.elearning.exception.InvalidAccessToken;
import eu.deltasource.elearning.exception.UserNotFoundException;
import eu.deltasource.elearning.model.User;
import eu.deltasource.elearning.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;

    @Test
    void givenNewUser_whenRegister_thenUserSavedAndTokensReturned() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@gmail.com");
        request.setPassword("password123");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        // When
        AuthResponse response = authService.register(request);

        // Then
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("INSTRUCTOR", response.getRole());
        assertEquals("John Doe", response.getFullName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void givenExistingEmail_whenRegister_thenThrowsUserNotFoundException() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@email.com");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(UserNotFoundException.class, () -> authService.register(request));
    }

    @Test
    void givenValidCredentials_whenLogin_thenTokensReturnedAndLastLoginUpdated() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setEmail("user@email.com");
        request.setPassword("pass");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setRole(Role.INSTRUCTOR);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        // When
        AuthResponse response = authService.login(request);

        // Then
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("INSTRUCTOR", response.getRole());
        assertEquals("Jane Smith", response.getFullName());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).save(user);
    }

    @Test
    void givenNonExistentUser_whenLogin_thenThrowsUserNotFoundException() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setEmail("notfound@email.com");
        request.setPassword("pass");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> authService.login(request));
    }

    @Test
    void givenValidCredentials_whenAuthenticate_thenTokensReturnedAndLastLoginUpdated() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setEmail("user@email.com");
        request.setPassword("pass");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setRole(Role.INSTRUCTOR);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        // When
        AuthResponse response = authService.authenticate(request);

        // Then
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("INSTRUCTOR", response.getRole());
        assertEquals("Jane Smith", response.getFullName());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).save(user);
    }

    @Test
    void givenNonExistentUser_whenAuthenticate_thenThrowsUserNotFoundException() {
        // Given
        AuthRequest request = new AuthRequest();
        request.setEmail("notfound@email.com");
        request.setPassword("pass");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> authService.authenticate(request));
    }

    @Test
    void givenValidRefreshToken_whenRefreshToken_thenReturnsNewTokens() {
        // Given
        String refreshToken = "Bearer validRefreshToken";
        String email = "user@email.com";
        User user = new User();
        user.setEmail(email);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setRole(Role.INSTRUCTOR);
        when(jwtService.extractUsername("validRefreshToken")).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("validRefreshToken", user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("newRefreshToken");

        // When
        AuthResponse response = authService.refreshToken(refreshToken);

        // Then
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("newRefreshToken", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("INSTRUCTOR", response.getRole());
        assertEquals("Jane Smith", response.getFullName());
    }

    @Test
    void givenInvalidRefreshToken_whenRefreshToken_thenThrowsRuntimeException() {
        // Given
        String refreshToken = "Bearer invalid";
        when(jwtService.extractUsername("invalid")).thenReturn(null);

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.refreshToken(refreshToken));
    }

    @Test
    void givenNonExistentUser_whenRefreshToken_thenThrowsUserNotFoundException() {
        // Given
        String refreshToken = "Bearer valid";
        when(jwtService.extractUsername("valid")).thenReturn("user@email.com");
        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> authService.refreshToken(refreshToken));
    }

    @Test
    void givenInvalidTokenValidity_whenRefreshToken_thenThrowsRuntimeException() {
        // Given
        String refreshToken = "Bearer valid";
        User user = new User();
        user.setEmail("user@email.com");
        when(jwtService.extractUsername("valid")).thenReturn("user@email.com");
        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid("valid", user)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.refreshToken(refreshToken));
    }

    @Test
    void givenExistingUser_whenUpdateProfile_thenUserUpdatedAndTokensReturned() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@email.com");
        request.setFirstName("New");
        request.setLastName("Name");
        request.setPassword("newpass");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName("Old");
        user.setLastName("Name");
        user.setRole(Role.INSTRUCTOR);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedNewPass");
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        // When
        AuthResponse response = authService.updateProfile(request);

        // Then
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("INSTRUCTOR", response.getRole());
        assertEquals("New Name", response.getFullName());
        verify(userRepository).save(user);
    }

    @Test
    void givenNonExistentUser_whenUpdateProfile_thenThrowsUserNotFoundException() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("notfound@email.com");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> authService.updateProfile(request));
    }

    @Test
    void givenValidAccessToken_whenLogout_thenReturnsNullTokens() {
        // Given
        String accessToken = "Bearer validToken";
        String email = "user@email.com";
        User user = new User();
        user.setEmail(email);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setRole(Role.INSTRUCTOR);
        when(jwtService.extractUsername("validToken")).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        AuthResponse response = authService.logout(accessToken);

        // Then
        assertNull(response.getAccessToken());
        assertNull(response.getRefreshToken());
        assertNull(response.getTokenType());
        assertNull(response.getRole());
        assertNull(response.getFullName());
    }

    @Test
    void givenInvalidAccessToken_whenLogout_thenThrowsInvalidAccessToken() {
        // Given
        String accessToken = "Bearer invalid";
        when(jwtService.extractUsername("invalid")).thenReturn(null);

        // When & Then
        assertThrows(InvalidAccessToken.class, () -> authService.logout(accessToken));
    }

    @Test
    void givenNonExistentUser_whenLogout_thenThrowsUserNotFoundException() {
        // Given
        String accessToken = "Bearer valid";
        when(jwtService.extractUsername("valid")).thenReturn("user@email.com");
        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> authService.logout(accessToken));
    }
}
