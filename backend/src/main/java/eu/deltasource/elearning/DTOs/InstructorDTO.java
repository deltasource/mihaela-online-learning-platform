package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.*;

/**
 * Data Transfer Object for Instructor entity.
 */
@Data
@Schema(description = "Instructor information")
public class InstructorDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Instructors email address", example = "eray.ali@example.com", requiredMode = REQUIRED)
    private String email;

    @NotBlank(message = "First name is required")
    @Schema(description = "Instructors first name", example = "Eray", requiredMode = REQUIRED)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Instructors last name", example = "Ali", requiredMode = REQUIRED)
    private String lastName;

    @NotBlank(message = "Department is required")
    @Schema(description = "Department name", example = "Software Engineering", requiredMode = REQUIRED)
    private String department;
}
