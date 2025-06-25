package eu.deltasource.elearning.DTOs;

import eu.deltasource.elearning.enums.Category;
import eu.deltasource.elearning.enums.Level;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Course information")
public class CourseDTO {
    @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank(message = "Course title is required")
    @Schema(description = "Course title", example = "Introduction to Web Development", requiredMode = REQUIRED)
    private String name;

    @NotBlank(message = "Course description is required")
    @Schema(description = "Course description", example = "Learn the fundamentals of web development", requiredMode = REQUIRED)
    private String description;

    @Schema(description = "Course thumbnail URL", example = "https://example.com/thumbnail.jpg")
    private String thumbnail;

    @Schema(description = "Instructor ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID instructorId;

    @Schema(description = "Instructor name", example = "Sarah Johnson")
    private String instructorName;

    @Schema(description = "Course category", example = "Web Development")
    private Category category;

    @Schema(description = "Course level", example = "BEGINNER")
    private Level level;

    @Schema(description = "Course duration in minutes", example = "1200")
    private Integer duration;

    @Schema(description = "Course average rating", example = "4.7")
    private Double rating;

    @Schema(description = "Number of ratings", example = "245")
    private Integer ratingCount;

    @Schema(description = "Number of enrolled students", example = "1345")
    private Integer enrollmentCount;

    @Schema(description = "Course price", example = "49.99")
    private BigDecimal price;

    @Schema(description = "Whether the course is published", example = "true")
    private Boolean isPublished;

    @Schema(description = "Course creation timestamp", example = "2023-01-15T10:30:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Course last update timestamp", example = "2023-06-20T14:45:00Z")
    private LocalDateTime updatedAt;

    @Schema(description = "Course lessons")
    private List<LessonDTO> lessons;

    @Schema(description = "Course quizzes")
    private List<QuizDTO> quizzes;

    @Schema(description = "IDs of enrolled students")
    private List<UUID> studentIds;
}
