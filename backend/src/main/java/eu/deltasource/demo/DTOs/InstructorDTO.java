package eu.deltasource.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the instructor data transfer object.
 */
@Schema(description = "Represents an instructor DTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstructorDTO {

    @Schema(description = "The person details of the instructor")
    private PersonDTO person;

    @Schema(description = "The department of the instructor", example = "Computer Science")
    private String department;

    @Schema(description = "The salary of the instructor", example = "75000")
    private double salary;

}
