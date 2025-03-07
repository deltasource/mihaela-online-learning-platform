package eu.deltasource.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Represents a person with common attributes.
 */
@Getter
@Setter
@NoArgsConstructor
public class Person {

    private int id;

    private String email;

    private String fullName;
}
