package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * REST controller for managing student-related operations.
 * This controller handles CRUD operations for students in the system.
 */
@RestController
@RequestMapping("/students/v1")
@Tag(name = "Student Management", description = "Operations pertaining to students in the system")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Create a new student", description = "Creates a new student with the provided details")
    public StudentDTO createStudent(
            @Parameter(description = "Student information for a new student to be created")
            @Valid @RequestBody StudentDTO studentDTO) {
        return studentService.createStudent(studentDTO);
    }

    @GetMapping("/{email}")
    @Operation(summary = "Get a student by email", description = "Returns a student based on the email provided")
    public StudentDTO getStudentByEmail(
            @Parameter(description = "Email of the student to be retrieved")
            @PathVariable @NotNull @Email(message = "Invalid email format") String email) {
        return studentService.getStudentByEmail(email);
    }

    @PutMapping("/{email}")
    @Operation(summary = "Update a student by email", description = "Updates a student's details based on the email provided")
    public StudentDTO updateStudentByEmail(
            @Parameter(description = "Email of the student to be updated")
            @PathVariable @NotNull @Email(message = "Invalid email format") String email,
            @Parameter(description = "Updated student information")
            @RequestBody @Valid StudentDTO studentDTO) {
        return studentService.updateStudentByEmail(email, studentDTO);
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Delete a student", description = "Deletes a student based on the email provided")
    @ResponseStatus(NO_CONTENT)
    public ResponseEntity<Void> deleteStudent(
            @Parameter(description = "Email of the student to be deleted")
            @PathVariable @NotNull @Email(message = "Invalid email format") String email) {
        studentService.deleteStudent(email);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a student by ID", description = "Returns a student based on the ID provided")
    public Student getStudentById(
            @Parameter(description = "ID of the student to be retrieved")
            @PathVariable @NotNull UUID id) {
        return studentService.getStudentById(id);
    }
}
