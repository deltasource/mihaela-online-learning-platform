package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for Instructor entity.
 */
@Data
@Schema(description = "Instructor information")
public class InstructorDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Person email address", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Full name is required")
    @Schema(description = "Person full name", example = "John Doe", required = true)
    private String fullName;

    @NotBlank(message = "Department is required")
    @Schema(description = "Department name", example = "Computer Science", required = true)
    private String department;
}
