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
    private UUID id;

    private String name;

    @Column(length = 1000)
    private String description;

    private String thumbnail;

    private String category;

    private String level;

    private int duration;

    private double rating;

    private int ratingCount;

    private int enrollmentCount;

    private double price;

    private boolean isPublished;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Quiz> quizzes;
}
