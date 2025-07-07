package eu.deltasource.elearning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Schema(description = "Lesson entity that belongs to a course")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique lesson identifier", example = "101")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @Schema(description = "Course to which this lesson belongs")
    private Course course;

    @Schema(description = "Lesson title", example = "Getting Started with HTML")
    private String title;

    @Schema(description = "Brief description of the lesson")
    private String description;

    @Schema(description = "Lesson content (HTML format allowed)")
    private String content;

    @Schema(description = "Video URL for the lesson", example = "https://www.youtube.com/embed/xyz")
    private String videoUrl;

    @Schema(description = "Duration of the lesson in minutes", example = "45")
    private int duration;

    @Schema(description = "Order of the lesson in the course", example = "1")
    private int order;

    @Schema(description = "Is the lesson published")
    private boolean isPublished;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
