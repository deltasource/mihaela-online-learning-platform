package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * Data Transfer Object for Course entity.
 */
@Data
@Schema(description = "Course information")
public class CourseDTO {

    @Schema(description = "Unique course identifier", example = "1")
    private UUID id;

    @NotBlank(message = "Course title is required")
    @Schema(description = "Title of the course", example = "Introduction to Web Development", requiredMode = REQUIRED)
    private String name;

    @NotBlank(message = "Course description is required")
    @Schema(description = "Detailed description of the course", example = "Learn web development fundamentals", requiredMode = REQUIRED)
    private String description;

    @Schema(description = "Thumbnail image URL", example = "https://example.com/image.jpg")
    private String thumbnail;

    @Schema(description = "Course category", example = "Web Development")
    private String category;

    @Schema(description = "Course difficulty level", example = "beginner")
    private String level;

    @PositiveOrZero
    @Schema(description = "Total duration of the course in minutes", example = "1200")
    private int duration;

    @Schema(description = "Average course rating", example = "4.7")
    private double rating;

    @Schema(description = "Number of ratings", example = "245")
    private int ratingCount;

    @Schema(description = "Number of students enrolled", example = "1345")
    private int enrollmentCount;

    @PositiveOrZero
    @Schema(description = "Course price in USD", example = "49.99")
    private double price;

    @Schema(description = "Is the course published or not")
    private boolean isPublished;

    @Schema(description = "Date and time when the course was created")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the course was last updated")
    private LocalDateTime updatedAt;

    @Schema(description = "ID of the instructor who created the course", example = "instructor-123")
    private UUID instructorId;

    @Schema(description = "List of lesson IDs in this course", example = "[\"lesson-1\", \"lesson-2\"]")
    private List<String> lessonIds;

    @Schema(description = "List of quiz IDs in this course", example = "[\"quiz-1\", \"quiz-2\"]")
    private List<String> quizIds;
}
