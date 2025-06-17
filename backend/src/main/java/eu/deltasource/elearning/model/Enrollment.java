package eu.deltasource.elearning.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    private LocalDateTime completedAt;

    private Double finalGrade;

    public enum EnrollmentStatus {
        ACTIVE, COMPLETED, DROPPED, SUSPENDED
    }
}
