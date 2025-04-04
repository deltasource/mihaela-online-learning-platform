package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

/**
 * Data Transfer Object for Student entity.
 */
@Data
@Schema(description = "Student information")
public class StudentDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Person email address", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Full name is required")
    @Schema(description = "Person full name", example = "John Doe", required = true)
    private String fullName;
}
