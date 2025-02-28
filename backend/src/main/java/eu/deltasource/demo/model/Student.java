package eu.deltasource.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Represents a student entity in the system.
 */
@Schema(description = "Represents a student in the system")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Schema(description = "The person details of the student")
    private Person person;

    @Schema(description = "The student number", example = "stu12345")
    private String studentNumber;
}
