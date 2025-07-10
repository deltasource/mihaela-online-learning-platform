package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * Data Transfer Object for Option entity.
 */
@Data
@Schema(description = "Option information for a quiz question")
public class OptionDTO {

    @Schema(description = "Unique option identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank(message = "Question ID is required")
    @Schema(description = "ID of the question this option belongs to", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = REQUIRED)
    private UUID questionId;

    @NotBlank(message = "Option text is required")
    @Schema(description = "Text of the option", example = "<ul>", requiredMode = REQUIRED)
    private String text;

    @Schema(description = "Is this the correct answer?", example = "true")
    private boolean isCorrect;
}
