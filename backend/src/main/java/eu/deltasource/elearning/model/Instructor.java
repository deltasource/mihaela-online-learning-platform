package eu.deltasource.elearning.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

/**
 * Entity class representing an instructor.
 * Contains instructor-specific information and a reference to the person.
 */
@Data
@Entity
@Table(name = "instructors")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String department;
}
