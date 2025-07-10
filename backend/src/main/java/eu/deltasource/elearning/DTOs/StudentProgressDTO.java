package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Student progress in a specific course")
public class StudentProgressDTO {

    @Schema(description = "Student's unique ID", example = "c7b76e5d-30f6-4962-a9e6-395b7a7898d6", requiredMode = REQUIRED)
    @NotNull(message = "Student ID cannot be null")
    @NotBlank(message = "Student ID cannot be blank")
    private String studentId;

    @Schema(description = "Course's unique ID", example = "df7c8c71-30f6-4962-b3c2-375a13ab8a97", requiredMode = REQUIRED)
    @NotNull(message = "Course ID cannot be null")
    @NotBlank(message = "Course ID cannot be blank")
    private String courseId;

    @Schema(description = "Percentage of the course completed by the student", example = "45.5", requiredMode = REQUIRED)
    @NotNull(message = "Progress percentage cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Progress percentage must be at least 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Progress percentage must not exceed 100")
    private Double progressPercentage;

    @Schema(description = "Total number of videos in the course", example = "10", requiredMode = REQUIRED)
    @NotNull(message = "Total number of videos cannot be null")
    @Min(value = 0, message = "Total number of videos cannot be negative")
    private Integer totalVideos;

    @Schema(description = "Number of videos watched by the student", example = "4", requiredMode = REQUIRED)
    @NotNull(message = "Number of videos watched cannot be null")
    @Min(value = 0, message = "Number of videos watched cannot be negative")
    private Integer videosWatched;
}
