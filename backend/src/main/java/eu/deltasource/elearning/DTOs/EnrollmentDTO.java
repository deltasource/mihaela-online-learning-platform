package eu.deltasource.elearning.DTOs;

import eu.deltasource.elearning.enums.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Enrollment information")
public class EnrollmentDTO {

    @Schema(description = "Enrollment ID")
    private UUID id;

    @NotNull(message = "Student ID is required")
    @Schema(description = "Student ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID studentId;

    @NotNull(message = "Course ID is required")
    @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID courseId;

    @Schema(description = "Enrollment status", example = "ACTIVE")
    private EnrollmentStatus status;

    @Schema(description = "Enrollment date")
    private LocalDateTime enrolledAt;

    @Schema(description = "Completion date")
    private LocalDateTime completedAt;

    @Schema(description = "Final grade", example = "85.5")
    private Double finalGrade;

    @Schema(description = "Student name")
    private String studentName;

    @Schema(description = "Course name")
    private String courseName;
}
