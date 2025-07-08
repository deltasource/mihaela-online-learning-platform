package eu.deltasource.elearning.DTOs;

import eu.deltasource.elearning.enums.Role;
import eu.deltasource.elearning.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "User registration request")
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User email", example = "user@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password123")
    private String password;

    @NotBlank(message = "First name is required")
    @Schema(description = "User first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "User last name", example = "Doe")
    private String lastName;

    @NotNull(message = "Role is required")
    @Schema(description = "User role", example = "STUDENT")
    private Role role;
}
