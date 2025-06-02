package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    @Operation(summary = "Create a new instructor", description = "Creates a new instructor in the system")
    public ResponseEntity<InstructorDTO> createInstructor(@RequestBody InstructorDTO instructorDTO) {
        InstructorDTO createdInstructor = instructorService.createInstructor(instructorDTO);
        return new ResponseEntity<>(createdInstructor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get instructor by ID", description = "Returns an instructor based on the provided ID")
    public ResponseEntity<InstructorDTO> getInstructorById(@PathVariable UUID id) {
        InstructorDTO instructor = instructorService.getInstructorById(id);
        return new ResponseEntity<>(instructor, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get instructor by email", description = "Returns an instructor based on the provided email")
    public ResponseEntity<InstructorDTO> getInstructorByEmail(@PathVariable String email) {
        InstructorDTO instructor = instructorService.getInstructorByEmail(email);
        return new ResponseEntity<>(instructor, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all instructors", description = "Returns a list of all instructors in the system")
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        List<InstructorDTO> instructors = instructorService.getAllInstructors();
        return new ResponseEntity<>(instructors, HttpStatus.OK);
    }

    @PutMapping("/email/{email}")
    @Operation(summary = "Update instructor by email", description = "Updates an instructor based on the provided email")
    public ResponseEntity<InstructorDTO> updateInstructorByEmail(@PathVariable String email, @RequestBody InstructorDTO instructorDTO) {
        InstructorDTO updatedInstructor = instructorService.updateInstructorByEmail(email, instructorDTO);
        return new ResponseEntity<>(updatedInstructor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete instructor by ID", description = "Deletes an instructor based on the provided ID")
    public ResponseEntity<Void> deleteInstructorById(@PathVariable UUID id) {
        instructorService.deleteInstructorById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/email/{email}")
    @Operation(summary = "Delete instructor by email", description = "Deletes an instructor based on the provided email")
    public ResponseEntity<Void> deleteInstructorByEmail(@PathVariable String email) {
        boolean deleted = instructorService.deleteInstructorByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
