package eu.deltasource.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents an instructor entity in the system.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Instructor {

    private Person person;

    private String department;

    private double salary;
}
