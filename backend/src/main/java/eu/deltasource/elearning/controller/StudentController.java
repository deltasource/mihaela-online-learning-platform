package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * REST controller for managing student-related operations.
 * This controller handles CRUD operations for students in the system.
 */
@RestController
@RequestMapping("/students/v1")
@Tag(name = "Student Management", description = "Operations pertaining to students in the system")
@Data
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Create a new student", description = "Creates a new student with the provided details")
    public StudentDTO createStudent(
            @Parameter(description = "Student information for a new student to be created")
            @Valid @RequestBody StudentDTO studentDTO) {
        log.info("Creating a new student with email: {}", studentDTO.getEmail());
        return studentService.createStudent(studentDTO);
    }

    @GetMapping("/{email}")
    @Operation(summary = "Get a student by email", description = "Returns a student based on the email provided")
    public StudentDTO getStudentByEmail(
            @Parameter(description = "Email of the student to be retrieved")
            @PathVariable @NotNull @Email(message = "Invalid email format") String email) {
        log.info("Retrieving student with email: {}", email);
        return studentService.getStudentByEmail(email);
    }

    @PutMapping("/{email}")
    @Operation(summary = "Update a student by email", description = "Updates a student's details based on the email provided")
    public StudentDTO updateStudentByEmail(
            @Parameter(description = "Email of the student to be updated")
            @PathVariable @NotNull @Email(message = "Invalid email format") String email,
            @Parameter(description = "Updated student information")
            @RequestBody @Valid StudentDTO studentDTO) {
        log.info("Updating student with email: {}", email);
        return studentService.updateStudentByEmail(email, studentDTO);
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Delete a student", description = "Deletes a student based on the email provided")
    @ResponseStatus(NO_CONTENT)
    public void deleteStudent(
            @Parameter(description = "Email of the student to be deleted")
            @PathVariable @NotNull @Email(message = "Invalid email format") String email) {
        log.info("Deleting student with email: {}", email);
        studentService.deleteStudent(email);
    }
}
