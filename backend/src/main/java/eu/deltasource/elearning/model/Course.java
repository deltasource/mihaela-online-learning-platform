package eu.deltasource.elearning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entity class representing a course.
 * Each course is associated with an instructor and may have multiple students and videos.
 */
@Entity
@Data
@Schema(description = "Course entity containing lessons and quizzes")
public class Course {

    @Id
    @Schema(description = "Unique course identifier", example = "1")
    private UUID id;

    @Schema(description = "Title of the course", example = "Introduction to Web Development")
    private String name;

    @Column(length = 1000)
    @Schema(description = "Detailed description of the course")
    private String description;

    @Schema(description = "Thumbnail image URL", example = "https://example.com/image.jpg")
    private String thumbnail;

    @Schema(description = "Course category", example = "Web Development")
    private String category;

    @Schema(description = "Course difficulty level", example = "beginner")
    private String level;

    @Schema(description = "Total duration of the course in minutes", example = "1200")
    private int duration;

    @Schema(description = "Average course rating", example = "4.7")
    private double rating;

    @Schema(description = "Number of ratings", example = "245")
    private int ratingCount;

    @Schema(description = "Number of students enrolled", example = "1345")
    private int enrollmentCount;

    @Schema(description = "Course price in USD", example = "49.99")
    private double price;

    @Schema(description = "Is the course published or not")
    private boolean isPublished;

    @Schema(description = "Date and time when the course was created")
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the course was last updated")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @Schema(description = "Instructor who created the course")
    private Instructor instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @Schema(description = "List of lessons in the course")
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @Schema(description = "List of quizzes in the course")
    private List<Quiz> quizzes;
}
