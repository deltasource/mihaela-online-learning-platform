package eu.deltasource.demo.controller;

import eu.deltasource.demo.DTOs.StudentDTO;
import eu.deltasource.demo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Creates a new student in the system.
     *
     * @param studentDTO the DTO containing the student information
     * @return the created StudentDTO
     */
    @PostMapping
    @Operation(summary = "Create a new student", description = "Creates a new student with the provided details")
    public StudentDTO createStudent(
            @Parameter(description = "Student information for a new student to be created")
            @Valid @RequestBody StudentDTO studentDTO) {
        return studentService.createStudent(studentDTO);
    }

    /**
     * Retrieves a student by their email address.
     *
     * @param email the email of the student to retrieve
     * @return the StudentDTO of the found student
     */
    @GetMapping("/{email}")
    @Operation(summary = "Get a student by email", description = "Returns a student based on the email provided")
    public StudentDTO getStudentByEmail(
            @Parameter(description = "Email of the student to be retrieved")
            @PathVariable String email) {
        return studentService.getStudentByEmail(email);
    }

    /**
     * Deletes a student from the system based on their email address.
     *
     * @param email the email of the student to delete
     * @return true if the student was successfully deleted, false otherwise
     */
    @DeleteMapping("/{email}")
    @Operation(summary = "Delete a student", description = "Deletes a student based on the email provided")
    public boolean deleteStudent(
            @Parameter(description = "Email of the student to be deleted")
            @PathVariable String email) {
        return studentService.deleteStudent(email);
    }
}
