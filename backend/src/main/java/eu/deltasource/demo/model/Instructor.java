package eu.deltasource.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents an instructor entity in the system.
 */
@Schema(description = "Represents an instructor in the system")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Instructor {

    @Schema(description = "The person details of the instructor")
    private Person person;

    @Schema(description = "The department of the instructor", example = "Computer Science")
    private String department;

    @Schema(description = "The salary of the instructor", example = "75000")
    private double salary;

}
