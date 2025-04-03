package eu.deltasource.elearning.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

/**
 * Entity class representing a student.
 * Contains student-specific information.
 */
@Data
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;
}
