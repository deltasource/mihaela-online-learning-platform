package eu.deltasource.demo.controller;

import eu.deltasource.demo.DTOs.StudentDTO;
import eu.deltasource.demo.model.Student;
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
            @PathVariable String email) {
        return studentService.getStudentByEmail(email);
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Delete a student", description = "Deletes a student based on the email provided")
    public boolean deleteStudent(
            @Parameter(description = "Email of the student to be deleted")
            @PathVariable String email) {
        return studentService.deleteStudent(email);
    }
}
