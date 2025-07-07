package eu.deltasource.elearning.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Unique quiz identifier", example = "201")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @Schema(description = "The course this quiz belongs to")
    private Course course;

    @Schema(description = "Quiz title", example = "HTML & CSS Quiz")
    private String title;

    @Schema(description = "Short description of the quiz")
    private String description;

    @Schema(description = "Passing score in percent", example = "70")
    private int passingScore;

    @Schema(description = "Time limit in minutes", example = "30")
    private int timeLimit;

    @Schema(description = "Order of the quiz in the course", example = "1")
    private int order;

    @Schema(description = "Is the quiz published")
    private boolean isPublished;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @Schema(description = "List of questions in the quiz")
    private List<Question> questions;
}
