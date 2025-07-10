package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * Data Transfer Object for Lesson entity.
 */
@Data
@Schema(description = "Lesson information")
public class LessonDTO {

    @Schema(description = "Unique lesson identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank(message = "Course ID is required")
    @Schema(description = "ID of the course this lesson belongs to", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = REQUIRED)
    private UUID courseId;

    @NotBlank(message = "Lesson title is required")
    @Schema(description = "Title of the lesson", example = "Getting Started with HTML", requiredMode = REQUIRED)
    private String title;

    @Schema(description = "Brief description of the lesson", example = "Learn the basics of HTML structure")
    private String description;

    @Schema(description = "Lesson content (HTML format allowed)")
    private String content;

    @Schema(description = "Video URL for the lesson", example = "https://www.youtube.com/embed/xyz")
    private String videoUrl;

    @PositiveOrZero(message = "Duration must be a positive number or zero")
    @Schema(description = "Duration of the lesson in minutes", example = "45")
    private int duration;

    @Min(value = 1, message = "Order must be at least 1")
    @Schema(description = "Order of the lesson in the course", example = "1")
    private int order;

    @Schema(description = "Is the lesson published")
    private boolean isPublished;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
