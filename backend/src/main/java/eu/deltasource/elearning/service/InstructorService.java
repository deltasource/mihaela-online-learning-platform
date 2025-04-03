package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.DTOs.PersonDTO;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.model.Person;
import eu.deltasource.elearning.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service class for managing instructor-related operations.
 * This class acts as an intermediary between the controller and the repository,
 * handling business logic for instructor management.
 */
@RequiredArgsConstructor
@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;

    /**
     * Creates a new instructor and saves it to the repository.
     *
     * @param instructorDTO The DTO containing the instructor details.
     * @return The created InstructorDTO.
     */
    @Transactional
    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        Instructor instructor = mapToInstructor(instructorDTO);
        if (instructor.getId() == null) {
            instructor.setId(UUID.randomUUID());
        }
        if (instructor.getPerson() != null && instructor.getPerson().getId() == null) {
            instructor.getPerson().setId(UUID.randomUUID());
        }
        instructor = instructorRepository.save(instructor);
        return mapToInstructorDTO(instructor);
    }

    /**
     * Retrieves an instructor by their email.
     *
     * @param email The email of the instructor.
     * @return The InstructorDTO with the instructor details.
     * @throws InstructorNotFoundException If the instructor is not found.
     */
    public InstructorDTO getInstructorByEmail(String email) {
        Instructor instructor = instructorRepository.findByPersonEmail(email)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with email " + email + " not found"));
        return mapToInstructorDTO(instructor);
    }

    /**
     * Updates an instructor by their email.
     *
     * @param email         The email of the instructor to update.
     * @param instructorDTO The DTO containing the updated instructor details.
     * @return The updated InstructorDTO.
     * @throws InstructorNotFoundException If the instructor is not found.
     */
    @Transactional
    public InstructorDTO updateInstructorByEmail(String email, InstructorDTO instructorDTO) {
        Instructor instructor = instructorRepository.findByPersonEmail(email)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with email " + email + " not found"));

        instructor.setDepartment(instructorDTO.getDepartment());
        if (instructorDTO.getId() != null) {
            instructor.setId(instructorDTO.getId());
        }
        if (instructorDTO.getPerson() != null) {
            instructor.getPerson().setFullName(instructorDTO.getPerson().getFullName());
        }
        instructor = instructorRepository.save(instructor);
        return mapToInstructorDTO(instructor);
    }

    /**
     * Deletes an instructor by their email.
     *
     * @param email The email of the instructor.
     * @return A boolean indicating whether the deletion was successful.
     * @throws InstructorNotFoundException If the instructor is not found.
     */
    @Transactional
    public boolean deleteInstructor(String email) {
        if (!instructorRepository.existsByPersonEmail(email)) {
            throw new InstructorNotFoundException("Instructor with email " + email + " not found");
        }
        int deletedCount = instructorRepository.deleteByPersonEmail(email);
        return deletedCount > 0;
    }

    /**
     * Maps an InstructorDTO to an Instructor entity.
     *
     * @param instructorDTO The DTO to map.
     * @return The mapped Instructor entity.
     */
    private Instructor mapToInstructor(InstructorDTO instructorDTO) {
        if (instructorDTO == null) {
            throw new IllegalArgumentException("InstructorDTO cannot be null");
        }

        Person person = new Person();
        if (instructorDTO.getPerson() != null) {
            person.setId(instructorDTO.getPerson().getId());
            person.setEmail(instructorDTO.getPerson().getEmail());
            person.setFullName(instructorDTO.getPerson().getFullName());
        }

        Instructor instructor = new Instructor();
        instructor.setId(instructorDTO.getId());
        instructor.setPerson(person);
        instructor.setDepartment(instructorDTO.getDepartment());

        return instructor;
    }

    /**
     * Maps an Instructor entity to an InstructorDTO.
     *
     * @param instructor The entity to map.
     * @return The mapped InstructorDTO.
     */
    private InstructorDTO mapToInstructorDTO(Instructor instructor) {
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor cannot be null");
        }

        PersonDTO personDTO = new PersonDTO();
        if (instructor.getPerson() != null) {
            personDTO.setId(instructor.getPerson().getId());
            personDTO.setEmail(instructor.getPerson().getEmail());
            personDTO.setFullName(instructor.getPerson().getFullName());
        }

        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setId(instructor.getId());
        instructorDTO.setPerson(personDTO);
        instructorDTO.setDepartment(instructor.getDepartment());

        return instructorDTO;
    }
}
