package eu.deltasource.demo.model;

import lombok.Data;

import java.util.UUID;

/**
 * Entity class representing an instructor.
 * Contains instructor-specific information and a reference to the person.
 */
@Data
public class Instructor {
    private UUID id;
    private Person person;
    private String department;
}
