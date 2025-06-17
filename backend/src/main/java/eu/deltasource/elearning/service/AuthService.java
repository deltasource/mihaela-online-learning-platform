package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.AuthRequest;
import eu.deltasource.elearning.DTOs.AuthResponse;
import eu.deltasource.elearning.DTOs.RegisterRequest;
import eu.deltasource.elearning.model.User;
import eu.deltasource.elearning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
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

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    public AuthResponse refreshToken(String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (jwtService.isTokenValid(refreshToken, user)) {
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
        throw new RuntimeException("Invalid refresh token");
    }
}
