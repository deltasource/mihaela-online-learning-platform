package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.AuthRequest;
import eu.deltasource.elearning.DTOs.AuthResponse;
import eu.deltasource.elearning.DTOs.RegisterRequest;
import eu.deltasource.elearning.exception.RefreshTokenException;
import eu.deltasource.elearning.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "User authentication and registration")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SecurityRequirements
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registering new user: {} {}, {}", request.getFirstName(), request.getLastName(), request.getEmail());
        return authService.register(request);
    }

    @Operation(summary = "Authenticate user")
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        log.info("User login attempt: {}", request.getEmail());
        return authService.login(request);
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestHeader("Authorization") String refreshToken) throws RefreshTokenException {
        log.info("Refreshing access token with refresh token: {}", refreshToken);
        return authService.refreshToken(refreshToken);
    }

    @PutMapping("update-profile")
    @Operation(summary = "Update user profile")
    public AuthResponse updateProfile(@Valid @RequestBody RegisterRequest request) {
        log.info("Updating user profile for: {} {}, {}", request.getFirstName(), request.getLastName(), request.getEmail());
        return authService.updateProfile(request);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public void logout(@RequestHeader("Authorization") String accessToken) {
        log.info("User logout attempt with access token: {}", accessToken);
        authService.logout(accessToken);
    }
}
