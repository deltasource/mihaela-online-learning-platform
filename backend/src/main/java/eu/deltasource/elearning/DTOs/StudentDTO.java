package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

/**
 * Data Transfer Object for Student entity.
 */
@Data
@Schema(description = "Student information")
public class StudentDTO {

    @Schema(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Valid
    @NotNull(message = "Person information is required")
    @Schema(description = "Person information", required = true)
    private PersonDTO person;
}
