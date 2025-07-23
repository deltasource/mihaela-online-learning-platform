package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.service.InstructorService;
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
 * REST controller for managing instructor-related operations.
 * This controller handles CRUD operations for instructors in the system.
 */
@RestController
@RequestMapping("/instructors/v1")
@Tag(name = "Instructor Management", description = "Operations pertaining to instructors in the system")
@Data
@Slf4j
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    @Operation(summary = "Create a new instructor", description = "Creates a new instructor with the provided details")
    public InstructorDTO createInstructor(
            @Parameter(description = "Instructor information for a new instructor to be created")
            @Valid @RequestBody InstructorDTO instructorDTO) {
        log.info("Creating a new instructor with email: {}", instructorDTO.getEmail());
        return instructorService.createInstructor(instructorDTO);
    }

    @GetMapping("/{email}")
    @Operation(summary = "Get an instructor by email", description = "Returns an instructor based on the email provided")
    public InstructorDTO getInstructorByEmail(
            @Parameter(description = "Email of the instructor to be retrieved")
            @PathVariable @NotNull @Email(message = "Invalid email format") String email) {
        log.info("Retrieving instructor with email: {}", email);
        return instructorService.getInstructorByEmail(email);
    }

    @PutMapping("/{email}")
    @Operation(summary = "Update an instructor by email", description = "Updates an instructor's details based on the email provided")
    public InstructorDTO updateInstructorByEmail(
            @Parameter(description = "Email of the instructor to be updated")
            @PathVariable @NotNull @Email(message = "Invalid email format") String email,
            @Parameter(description = "Updated instructor information")
            @RequestBody @Valid InstructorDTO instructorDTO) {
        log.info("Updating instructor with email: {}", email);
        return instructorService.updateInstructorByEmail(email, instructorDTO);
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Delete an instructor", description = "Deletes an instructor based on the email provided")
    @ResponseStatus(NO_CONTENT)
    public void deleteInstructor(
            @Parameter(description = "Email of the instructor to be deleted")
            @PathVariable @NotNull @Email(message = "Invalid email format") String email) {
        log.info("Deleting instructor with email: {}", email);
        instructorService.deleteInstructor(email);
    }
}
