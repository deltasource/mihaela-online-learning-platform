package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable @NotNull String email) {
        return studentService.getStudentByEmail(email);
    }

    @PutMapping("/{email}")
    @Operation(summary = "Update a student by email", description = "Updates a student's details based on the email provided")
    public StudentDTO updateStudentByEmail(
            @Parameter(description = "Email of the student to be updated")
            @PathVariable @NotNull String email,
            @Parameter(description = "Updated student information")
            @RequestBody @Valid StudentDTO studentDTO) {
        return studentService.updateStudentByEmail(email, studentDTO);
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Delete a student", description = "Deletes a student based on the email provided")
    @ResponseStatus(NO_CONTENT)
    public void deleteInstructor(
            @Parameter(description = "Email of the student to be deleted")
            @PathVariable String email) {
        studentService.deleteStudent(email);
    }
}
