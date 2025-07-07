package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * Data Transfer Object for Question entity.
 */
@Data
@Schema(description = "Question information for a quiz")
public class QuestionDTO {

    @Schema(description = "Unique question identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank(message = "Quiz ID is required")
    @Schema(description = "ID of the quiz this question belongs to", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = REQUIRED)
    private UUID quizId;

    @NotBlank(message = "Question text is required")
    @Schema(description = "The question text", example = "Which HTML element is used to define an unordered list?", requiredMode = REQUIRED)
    private String question;

    @NotBlank(message = "Question type is required")
    @Schema(description = "Type of the question", example = "multiple-choice", requiredMode = REQUIRED)
    private String questionType;

    @Min(value = 1, message = "Points must be at least 1")
    @Schema(description = "Number of points this question is worth", example = "10")
    private int points;

    @Schema(description = "List of option IDs for this question")
    private List<UUID> optionIds;
}
