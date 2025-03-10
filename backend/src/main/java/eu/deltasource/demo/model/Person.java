package eu.deltasource.demo.model;

import lombok.Data;

import java.util.UUID;

/**
 * Entity class representing a person.
 * Contains basic personal information.
 */
@Data
public class Person {
    private UUID id;
    private String email;
    private String fullName;
}
