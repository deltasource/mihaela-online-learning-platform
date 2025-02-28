package eu.deltasource.demo.service;

import eu.deltasource.demo.DTOs.InstructorDTO;
import eu.deltasource.demo.DTOs.PersonDTO;
import eu.deltasource.demo.exception.InstructorNotFoundException;
import eu.deltasource.demo.model.Instructor;
import eu.deltasource.demo.model.Person;
import eu.deltasource.demo.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        instructorRepository.save(instructor);
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
        Instructor instructor = instructorRepository.getByEmail(email)
                .orElseThrow(() -> new InstructorNotFoundException("Instructor with email " + email + " not found"));
        return mapToInstructorDTO(instructor);
    }

    /**
     * Deletes an instructor by their email.
     *
     * @param email The email of the instructor.
     * @return A boolean indicating whether the deletion was successful.
     * @throws InstructorNotFoundException If the instructor is not found.
     */
    public boolean deleteInstructor(String email) {
        if (!instructorRepository.remove(email)) {
            throw new InstructorNotFoundException("Instructor with email " + email + " not found");
        }
        return true;
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
        instructor.setPerson(person);
        instructor.setDepartment(instructorDTO.getDepartment());
        instructor.setSalary(instructorDTO.getSalary());

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
        instructorDTO.setPerson(personDTO);
        instructorDTO.setDepartment(instructor.getDepartment());
        instructorDTO.setSalary(instructor.getSalary());

        return instructorDTO;
    }
}
