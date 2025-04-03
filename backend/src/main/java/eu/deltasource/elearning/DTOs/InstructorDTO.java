package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for Instructor entity.
 */
@Data
@Schema(description = "Instructor information")
public class InstructorDTO {

    @Valid
    @NotNull(message = "Person information is required")
    @Schema(description = "Person information", required = true)
    private PersonDTO person;

    @NotBlank(message = "Department is required")
    @Schema(description = "Department name", example = "Computer Science", required = true)
    private String department;
}
