package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Quiz information")
public class QuizDTO {
    @Schema(description = "Quiz ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID courseId;

    @NotBlank(message = "Quiz title is required")
    @Schema(description = "Quiz title", example = "HTML & CSS Quiz", requiredMode = REQUIRED)
    private String title;

    @Schema(description = "Quiz description", example = "Test your knowledge of HTML and CSS")
    private String description;

    @Schema(description = "Passing score percentage", example = "70")
    private Integer passingScore;

    @Schema(description = "Time limit in minutes", example = "30")
    private Integer timeLimit;

    @Schema(description = "Quiz order in the course", example = "1")
    private Integer order;

    @Schema(description = "Whether the quiz is published", example = "true")
    private Boolean isPublished;

    @Schema(description = "Quiz creation timestamp", example = "2023-01-20T10:30:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Quiz last update timestamp", example = "2023-01-20T10:30:00Z")
    private LocalDateTime updatedAt;

    @Schema(description = "Quiz questions")
    private List<QuestionDTO> questions;
}
