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
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;

    private String description;

    private String content;

    private String videoUrl;

    private int duration;

    private int order;

    private boolean isPublished;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
