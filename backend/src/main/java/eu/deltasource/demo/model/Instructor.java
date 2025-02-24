package eu.deltasource.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Represents an instructor entity in the system.
 * This class encapsulates the core attributes of an instructor.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Represents an instructor in the system")
public class Instructor {

    @Schema(description = "The unique identifier of the instructor", example = "1")
    private int id;

    @Schema(description = "The email address of the instructor", example = "instructor@example.com")
    private String email;

    @Schema(description = "The full name of the instructor", example = "Dr. Jane Doe")
    private String fullName;

    @Schema(description = "The department of the instructor", example = "Computer Science")
    private String department;

    @Schema(description = "The salary of the instructor", example = "75000")
    private double salary;
}
