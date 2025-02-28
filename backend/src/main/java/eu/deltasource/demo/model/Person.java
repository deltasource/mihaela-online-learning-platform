package eu.deltasource.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Represents a person with common attributes.
 */
@Schema(description = "Represents a person with common attributes")
@Getter
@Setter
@NoArgsConstructor
public class Person {

    @Schema(description = "The unique identifier of the person", example = "1")
    private int id;

    @Schema(description = "The email address of the person", example = "person@example.com")
    private String email;

    @Schema(description = "The full name of the person", example = "John Doe")
    private String fullName;
}
