package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.exception.InstructorAlreadyExistsException;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.repository.InstructorRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
public class InstructorService {

    private final InstructorRepository instructorRepository;

    @Transactional
    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        Instructor instructor = mapToInstructor(instructorDTO);
        Optional<Instructor> existingInstructor = instructorRepository.findByEmail(instructor.getEmail());
        if (existingInstructor.isPresent()) {
            throw new InstructorAlreadyExistsException("Instructor with email " + instructor.getEmail() + " already exists.");
        }
        instructor = instructorRepository.save(instructor);
        return mapToInstructorDTO(instructor);
    }

    public InstructorDTO getInstructorByEmail(String email) {
        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with email " + email + " not found"));
        return mapToInstructorDTO(instructor);
    }

    @Transactional
    public InstructorDTO updateInstructorByEmail(String email, InstructorDTO instructorDTO) {
        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with email " + email + " not found"));

        instructor.setDepartment(instructorDTO.getDepartment());
        if (instructorDTO.getEmail() != null) {
            instructor.setFirstName(instructorDTO.getFirstName());
            instructor.setLastName(instructorDTO.getLastName());
        }
        instructor = instructorRepository.save(instructor);
        return mapToInstructorDTO(instructor);
    }

    @Transactional
    public boolean deleteInstructor(String email) {
        if (!instructorRepository.existsByEmail(email)) {
            throw new InstructorNotFoundException("Instructor with email " + email + " not found");
        }
        int deletedCount = instructorRepository.deleteByEmail(email);
        return deletedCount > 0;
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
        instructorDTO.setDepartment(instructor.getDepartment());
        return instructorDTO;
    }
}
