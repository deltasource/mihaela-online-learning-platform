package eu.deltasource.elearning.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

/**
 * Entity class representing a person.
 * Contains basic personal information.
 */
@Data
@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;
}
