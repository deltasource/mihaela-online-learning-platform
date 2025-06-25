package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Option information")
public class OptionDTO {
    @Schema(description = "Option ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Question ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID questionId;

    @NotBlank(message = "Option text is required")
    @Schema(description = "Option text", example = "<ul>", requiredMode = REQUIRED)
    private String text;

    @Schema(description = "Whether this option is correct", example = "true")
    private Boolean isCorrect;
}
