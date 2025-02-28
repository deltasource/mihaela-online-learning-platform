package eu.deltasource.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the person data transfer object.
 */
@Schema(description = "Represents a person DTO")
@Getter
@Setter
public class PersonDTO {

    @Schema(description = "The unique identifier of the person", example = "1")
    private int id;

    @Schema(description = "The email address of the person", example = "person@example.com")
    private String email;

    @Schema(description = "The full name of the person", example = "John Doe")
    private String fullName;

}
