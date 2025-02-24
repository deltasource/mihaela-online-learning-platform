package eu.deltasource.demo.service;

import eu.deltasource.demo.DTOs.InstructorDTO;
import eu.deltasource.demo.exception.InstructorNotFoundException;
import eu.deltasource.demo.model.Instructor;
import eu.deltasource.demo.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        Instructor instructor = new Instructor(instructorDTO.getId(), instructorDTO.getEmail(),
                instructorDTO.getFullName(), instructorDTO.getDepartment(), instructorDTO.getSalary());
        instructorRepository.save(instructor);
        return new InstructorDTO(instructor.getId(), instructor.getEmail(), instructor.getFullName(),
                instructor.getDepartment(), instructor.getSalary());
    }

    public InstructorDTO getInstructorByEmail(String email) {
        Optional<Instructor> optionalInstructor = instructorRepository.getByEmail(email);

        if (optionalInstructor.isEmpty()) {
            throw new InstructorNotFoundException(email);
        }
        Instructor instructor = optionalInstructor.get();
        return new InstructorDTO(instructor.getId(), instructor.getEmail(), instructor.getFullName(),
                instructor.getDepartment(), instructor.getSalary());
    }

    public boolean deleteInstructor(String email) {
        return instructorRepository.remove(email);
    }
}
