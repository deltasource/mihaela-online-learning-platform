package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Video information")
public class VideoDTO {

    @Schema(description = "Video file name", example = "introduction.mp4")
    private String fileName;

    @Schema(description = "Video file path", example = "/path/to/videos/introduction.mp4")
    private String filePath;

    @NotNull(message = "Course ID is required")
    @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = REQUIRED)
    private UUID courseId;
}
