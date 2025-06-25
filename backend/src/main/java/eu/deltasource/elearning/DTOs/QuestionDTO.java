package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Question information")
public class QuestionDTO {
    @Schema(description = "Question ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Quiz ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID quizId;

    @NotBlank(message = "Question text is required")
    @Schema(description = "Question text", example = "Which HTML element is used to define an unordered list?", requiredMode = REQUIRED)
    private String question;

    @Schema(description = "Question type", example = "MULTIPLE_CHOICE")
    private String questionType;

    @Schema(description = "Points value", example = "10")
    private Integer points;

    @Schema(description = "Answer options")
    private List<OptionDTO> options;
}
