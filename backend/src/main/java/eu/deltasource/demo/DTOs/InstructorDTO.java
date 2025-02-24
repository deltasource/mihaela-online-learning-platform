package eu.deltasource.demo.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/**
 * Data Transfer Object (DTO) representing an instructor in the system.
 * This class encapsulates instructor information for transfer between layers of the application.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Details about the instructor")
public class InstructorDTO {

    @Schema(description = "The unique identifier of the instructor", example = "1")
    private int id;

    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Schema(description = "The email address of the instructor", example = "instructor@example.com", required = true)
    private String email;

    @NotEmpty(message = "Full name is required.")
    @Schema(description = "The full name of the instructor", example = "Dr. Jane Doe", required = true)
    private String fullName;

    @NotEmpty(message = "Department is required.")
    @Schema(description = "The department of the instructor", example = "Computer Science", required = true)
    private String department;

    @Schema(description = "The salary of the instructor", example = "75000")
    private double salary;
}
