package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * Data Transfer Object for Student entity.
 */
@Data
@Schema(description = "Student information")
public class StudentDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "Students email address", example = "mihaela.kolarova@example.com",requiredMode = REQUIRED)
    private String email;

    @NotBlank(message = "Full name is required")
    @Schema(description = "Students first name", example = "Mihaela", requiredMode = REQUIRED)
    private String firstName;

    @NotBlank(message = "Full name is required")
    @Schema(description = "Students last name", example = "Kolarova", requiredMode = REQUIRED)
    private String lastName;

}
