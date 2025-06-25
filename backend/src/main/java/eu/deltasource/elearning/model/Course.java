package eu.deltasource.elearning.model;

    import eu.deltasource.elearning.enums.Category;
    import eu.deltasource.elearning.enums.Level;
    import jakarta.persistence.*;
    import lombok.Data;
    import java.math.BigDecimal;
    import java.time.Instant;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.UUID;

    @Data
    @Entity
    @Table(name = "courses")
    public class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false, length = 1000)
        private String description;

        private String thumbnail;

        @ManyToOne
        @JoinColumn(name = "instructor_id", referencedColumnName = "id")
        private Instructor instructor;

        @Enumerated(EnumType.STRING)
        private Category category;

        @Enumerated(EnumType.STRING)
        private Level level;

        private Integer duration;

        private Double rating;

        private Integer ratingCount;

        private Integer enrollmentCount;

        private BigDecimal price;

        private boolean isPublished;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
        private List<Lesson> lessons;

        @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
        private List<Quiz> quizzes;

        @ManyToMany
        @JoinTable(
                name = "course_students",
                joinColumns = @JoinColumn(name = "course_id"),
                inverseJoinColumns = @JoinColumn(name = "student_id")
        )
        private List<Student> students;
    }
