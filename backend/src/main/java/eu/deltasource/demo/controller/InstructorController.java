package eu.deltasource.demo.controller;

import eu.deltasource.demo.DTOs.InstructorDTO;
import eu.deltasource.demo.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing instructor-related operations.
 * This controller handles CRUD operations for instructors in the system.
 */
@RestController
@RequestMapping("/instructors/v1")
@Tag(name = "Instructor Management", description = "Operations pertaining to instructors in the system")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    /**
     * Creates a new instructor in the system.
     *
     * @param instructorDTO the DTO containing the instructor information
     * @return the created InstructorDTO
     */
    @PostMapping
    @Operation(summary = "Create a new instructor", description = "Creates a new instructor with the provided details")
    public InstructorDTO createInstructor(
            @Parameter(description = "Instructor information for a new instructor to be created")
            @Valid @RequestBody InstructorDTO instructorDTO) {
        return instructorService.createInstructor(instructorDTO);
    }

    /**
     * Retrieves an instructor by their email address.
     *
     * @param email the email of the instructor to retrieve
     * @return the InstructorDTO of the found instructor
     */
    @GetMapping("/{email}")
    @Operation(summary = "Get an instructor by email", description = "Returns an instructor based on the email provided")
    public InstructorDTO getInstructorByEmail(
            @Parameter(description = "Email of the instructor to be retrieved")
            @PathVariable @NotNull String email) {
        return instructorService.getInstructorByEmail(email);
    }

    /**
     * Updates an instructor by their email address.
     *
     * @param email the email of the instructor to update
     * @param instructorDTO the DTO containing the updated instructor information
     * @return the updated InstructorDTO
     */
    @PutMapping("/{email}")
    @Operation(summary = "Update an instructor by email", description = "Updates an instructor's details based on the email provided")
    public InstructorDTO updateInstructorByEmail(
            @Parameter(description = "Email of the instructor to be updated")
            @PathVariable @NotNull String email,
            @Parameter(description = "Updated instructor information")
            @RequestBody @Valid InstructorDTO instructorDTO) {
        return instructorService.updateInstructorByEmail(email, instructorDTO);
    }

    /**
     * Deletes an instructor from the system based on their email address.
     *
     * @param email the email of the instructor to delete
     * @return true if the instructor was successfully deleted, false otherwise
     */
    @DeleteMapping("/{email}")
    @Operation(summary = "Delete an instructor", description = "Deletes an instructor based on the email provided")
    public boolean deleteInstructor(
            @Parameter(description = "Email of the instructor to be deleted")
            @PathVariable String email) {
        return instructorService.deleteInstructor(email);
    }
}
