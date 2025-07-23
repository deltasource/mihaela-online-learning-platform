package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.AuthRequest;
import eu.deltasource.elearning.DTOs.AuthResponse;
import eu.deltasource.elearning.DTOs.RegisterRequest;
import eu.deltasource.elearning.exception.InvalidAccessToken;
import eu.deltasource.elearning.exception.UserNotFoundException;
import eu.deltasource.elearning.model.User;
import eu.deltasource.elearning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static eu.deltasource.elearning.enums.Role.INSTRUCTOR;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(RegisterRequest request) {
        log.info("Registering request: {}", request);
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Attempt to register with existing email: {}", request.getEmail());
            throw new UserNotFoundException("User with this email already exists");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(INSTRUCTOR)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(
                jwtToken,
                refreshToken,
                "Bearer",
                user.getRole().name(),
                user.getFirstName() + " " + user.getLastName()
        );
    }

    public AuthResponse login(AuthRequest request) {
        log.info("Login request: {}", request);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        log.info("Authentication successful for user: {}", request.getEmail());
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        log.info("User found: {}", user.getEmail());
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(
                jwtToken,
                refreshToken,
                "Bearer",
                user.getRole().name(),
                user.getFirstName() + " " + user.getLastName()
        );
    }

    public AuthResponse authenticate(AuthRequest request) {
        log.info("Authenticating user: {}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        log.info("User authenticated: {}", user.getEmail());
        user.setLastLoginAt(LocalDateTime.now());
        log.info("User last login at: {}", user.getLastLoginAt());
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(
                jwtToken,
                refreshToken,
                "Bearer",
                user.getRole().name(),
                user.getFirstName() + " " + user.getLastName()
        );
    }

    public AuthResponse refreshToken(String refreshToken) {
        log.info("Refreshing token: {}", refreshToken);
        if (refreshToken.startsWith("Bearer ")) {
            log.info("Stripping 'Bearer ' prefix from refresh token");
            refreshToken = refreshToken.substring(7);
        }
        log.info("Extracting username from refresh token");

        log.info("Username from refresh token: {}", refreshToken);
        String userEmail = jwtService.extractUsername(refreshToken);
        log.info("User email extracted: {}", userEmail);
        if (userEmail != null) {
            log.info("Finding user by email: {}", userEmail);
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            log.info("User found: {}", user.getEmail());
            if (jwtService.isTokenValid(refreshToken, user)) {
                log.info("Refresh token is valid for user: {}", user.getEmail());
                var accessToken = jwtService.generateToken(user);
                var newRefreshToken = jwtService.generateRefreshToken(user);

                return new AuthResponse(
                        accessToken,
                        newRefreshToken,
                        "Bearer",
                        user.getRole().name(),
                        user.getFirstName() + " " + user.getLastName()
                );
            }
        }
        throw new InvalidAccessToken("Invalid refresh token");
    }

    public AuthResponse updateProfile(RegisterRequest request) {
        log.info("Updating profile for user: {}", request.getEmail());
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        log.info("User found: {}", user.getEmail());
        user.setFirstName(request.getFirstName());
        log.info("Updating first name to: {}", request.getFirstName());
        user.setLastName(request.getLastName());
        log.info("Updating last name to: {}", request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        log.info("Updating password for user: {}", user.getEmail());
        user.setRole(INSTRUCTOR);

        log.info("Setting role to INSTRUCTOR for user: {}", user.getEmail());
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(
                jwtToken,
                refreshToken,
                "Bearer",
                user.getRole().name(),
                user.getFirstName() + " " + user.getLastName()
        );
    }

    public AuthResponse logout(String accessToken) {
        log.info("Logging out user with access token: {}", accessToken);
        if (accessToken.startsWith("Bearer ")) {
            log.info("Stripping 'Bearer ' prefix from access token");
            accessToken = accessToken.substring(7);
        }
        log.info("Extracting username from access token");
        String userEmail = jwtService.extractUsername(accessToken);
        log.info("User email extracted: {}", userEmail);
        if (userEmail != null) {
            log.info("Finding user by email: {}", userEmail);
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            return new AuthResponse(null, null, null, null, null);
        }
        throw new InvalidAccessToken("Invalid access token");
    }
}
