package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * Data Transfer Object for Quiz entity.
 */
@Data
@Schema(description = "Quiz information")
public class QuizDTO {

    @Schema(description = "Unique quiz identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank(message = "Course ID is required")
    @Schema(description = "ID of the course this quiz belongs to", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = REQUIRED)
    private UUID courseId;

    @NotBlank(message = "Quiz title is required")
    @Schema(description = "Title of the quiz", example = "HTML Fundamentals Quiz", requiredMode = REQUIRED)
    private String title;

    @Schema(description = "Brief description of the quiz", example = "Test your knowledge of HTML basics")
    private String description;

    @Min(value = 1, message = "Passing score must be at least 1 percent")
    @Schema(description = "Passing score in percent", example = "70")
    private int passingScore;

    @PositiveOrZero(message = "Time limit must be a positive number or zero")
    @Schema(description = "Time limit in minutes", example = "30")
    private int timeLimit;

    @Min(value = 1, message = "Order must be at least 1")
    @Schema(description = "Order of the quiz in the course", example = "1")
    private int order;

    @Schema(description = "Is the quiz published")
    private boolean isPublished;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @Schema(description = "List of question IDs in this quiz")
    private List<UUID> questionIds;
}
