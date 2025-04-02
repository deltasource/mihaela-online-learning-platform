package eu.deltasource.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;
import java.util.List;

/**
 * Data Transfer Object for Course entity.
 */
@Data
@Schema(description = "Course information")
public class CourseDTO {

    @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank(message = "Course name is required")
    @Schema(description = "Course name", example = "Java Programming", required = true)
    private String name;

    @NotBlank(message = "Course description is required")
    @Schema(description = "Course description", example = "Learn Java from scratch", required = true)
    private String description;

    @Schema(description = "Instructor ID for the course", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID instructorId;

    @Schema(description = "List of students enrolled in the course", example = "[\"123e4567-e89b-12d3-a456-426614174000\"]")
    private List<UUID> studentIds;
}
