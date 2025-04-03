package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

/**
 * Data Transfer Object for Instructor entity.
 */
@Data
@Schema(description = "Instructor information")
public class InstructorDTO {

    @Schema(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Valid
    @NotNull(message = "Person information is required")
    @Schema(description = "Person information", required = true)
    private PersonDTO person;

    @NotBlank(message = "Department is required")
    @Schema(description = "Department name", example = "Computer Science", required = true)
    private String department;
}
