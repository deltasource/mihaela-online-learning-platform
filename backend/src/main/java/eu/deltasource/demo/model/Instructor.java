package eu.deltasource.demo.model;

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
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Column(nullable = false)
    private String department;
}
