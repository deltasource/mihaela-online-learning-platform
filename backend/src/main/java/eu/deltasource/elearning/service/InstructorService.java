package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.exception.InstructorAlreadyExistsException;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.repository.InstructorRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing instructor-related operations.
 * This class acts as an intermediary between the controller and the repository,
 * handling business logic for instructor management.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class InstructorService {

    private final InstructorRepository instructorRepository;

    @Transactional
    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        log.info("Creating instructor: {}", instructorDTO);
        Instructor instructor = mapToInstructor(instructorDTO);
        Optional<Instructor> existingInstructor = instructorRepository.findByEmail(instructor.getEmail());
        log.info("Checking for existing instructor with email: {}", instructor.getEmail());
        if (existingInstructor.isPresent()) {
            log.warn("Instructor with email {} already exists", instructor.getEmail());
            throw new InstructorAlreadyExistsException("Instructor with email " + instructor.getEmail() + " already exists.");
        }
        instructor = instructorRepository.save(instructor);
        return mapToInstructorDTO(instructor);
    }

    public InstructorDTO getInstructorByEmail(String email) {
        log.info("Retrieving instructor with email: {}", email);
        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with email " + email + " not found"));
        return mapToInstructorDTO(instructor);
    }

    @Transactional
    public InstructorDTO updateInstructorByEmail(String email, InstructorDTO instructorDTO) {
        log.info("Updating instructor with email: {}", email);
        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with email " + email + " not found"));

        instructor.setDepartment(instructorDTO.getDepartment());
        log.info("Checking for instructor with email: {}", instructor.getEmail());
        if (instructorDTO.getEmail() != null) {
            log.warn("Instructor with email {} already exists", instructor.getEmail());
            instructor.setFirstName(instructorDTO.getFirstName());
            instructor.setLastName(instructorDTO.getLastName());
        }
        instructor = instructorRepository.save(instructor);
        log.info("Instructor with email {} updated successfully", instructor.getEmail());
        return mapToInstructorDTO(instructor);
    }

    @Transactional
    public void deleteInstructor(String email) {
        log.info("Deleting instructor with email: {}", email);
        if (!instructorRepository.existsByEmail(email)) {
            log.warn("Instructor with email {} not found", email);
            throw new InstructorNotFoundException("Instructor with email " + email + " not found");
        }
        int deletedCount = instructorRepository.deleteByEmail(email);
        log.info("Deleted {} instructor(s) with email: {}", deletedCount, email);
        if (deletedCount == 0) {
            log.warn("No instructor found with email: {}", email);
            throw new InstructorNotFoundException("Instructor with email " + email + " not found");
        }
    }

    @NotNull
    private Instructor mapToInstructor(InstructorDTO instructorDTO) {
        Instructor instructor = new Instructor();
        instructor.setEmail(instructorDTO.getEmail());
        instructor.setFirstName(instructorDTO.getFirstName());
        instructor.setLastName(instructorDTO.getLastName());
        instructor.setDepartment(instructorDTO.getDepartment());
        return instructor;
    }

    @NotNull
    private InstructorDTO mapToInstructorDTO(Instructor instructor) {
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setEmail(instructor.getEmail());
        instructorDTO.setFirstName((instructor.getFirstName()));
        instructorDTO.setLastName(instructor.getLastName());
        instructorDTO.setDepartment(instructor.getDepartment());
        return instructorDTO;
    }
}
