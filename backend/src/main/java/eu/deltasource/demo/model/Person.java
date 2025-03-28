package eu.deltasource.demo.model;

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
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;
}
