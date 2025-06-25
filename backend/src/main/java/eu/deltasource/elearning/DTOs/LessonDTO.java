package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Lesson information")
public class LessonDTO {
    @Schema(description = "Lesson ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID courseId;

    @NotBlank(message = "Lesson title is required")
    @Schema(description = "Lesson title", example = "Getting Started with HTML", requiredMode = REQUIRED)
    private String title;

    @Schema(description = "Lesson description", example = "Learn the basics of HTML")
    private String description;

    @Schema(description = "Lesson content in HTML format")
    private String content;

    @Schema(description = "Video URL", example = "https://www.youtube.com/embed/UB1O30fR-EE")
    private String videoUrl;

    @Schema(description = "Lesson duration in minutes", example = "45")
    private Integer duration;

    @Schema(description = "Lesson order in the course", example = "1")
    private Integer order;

    @Schema(description = "Whether the lesson is published", example = "true")
    private Boolean isPublished;

    @Schema(description = "Lesson creation timestamp", example = "2023-01-15T10:30:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Lesson last update timestamp", example = "2023-01-15T10:30:00Z")
    private LocalDateTime updatedAt;
}
