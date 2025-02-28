package eu.deltasource.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the student data transfer object.
 */
@Schema(description = "Represents a student DTO")
@Getter
@Setter
public class StudentDTO {

    @Schema(description = "The person details of the student")
    private PersonDTO person;

    @Schema(description = "The student number", example = "S12345")
    private String studentNumber;

}
