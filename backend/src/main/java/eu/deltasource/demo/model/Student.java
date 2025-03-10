package eu.deltasource.demo.model;

import lombok.Data;

import java.util.UUID;

/**
 * Entity class representing a student.
 * Contains student-specific information.
 */
@Data
public class Student {
    private UUID id;
    private String email;
    private String fullName;
}
