package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.PersonDTO;
import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.exception.StudentNotFoundException;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service class for managing student-related operations.
 * This class acts as an intermediary between the controller and the repository,
 * handling business logic for student management.
 */
@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    /**
     * Creates a new student and saves it to the repository.
     *
     * @param studentDTO The DTO containing the student details.
     * @return The created StudentDTO.
     */
    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = mapToStudent(studentDTO);

        Student savedStudent = studentRepository.save(student);
        return mapToStudentDTO(savedStudent);
    }

    /**
     * Retrieves a student by their email.
     *
     * @param email The email of the student.
     * @return The StudentDTO with the student details.
     * @throws StudentNotFoundException If the student is not found.
     */
    public StudentDTO getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException("Student with email " + email + " not found"));
        return mapToStudentDTO(student);
    }

    /**
     * Updates a student by their email.
     *
     * @param email The email of the student to update.
     * @param studentDTO The DTO containing the updated student details.
     * @return The updated StudentDTO.
     * @throws StudentNotFoundException If the student is not found.
     */
    @Transactional
    public StudentDTO updateStudentByEmail(String email, StudentDTO studentDTO) {
        Student existingStudent = studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException("Student with email " + email + " not found"));

        Student student = mapToStudent(studentDTO);
        student.setId(existingStudent.getId());
        student.setEmail(email);

        Student updatedStudent = studentRepository.save(student);
        return mapToStudentDTO(updatedStudent);
    }

    /**
     * Deletes a student by their email.
     *
     * @param email The email of the student to delete.
     * @return A boolean indicating whether the deletion was successful.
     * @throws StudentNotFoundException If the student is not found.
     */
    @Transactional
    public boolean deleteStudent(String email) {
        if (!studentRepository.existsByEmail(email)) {
            throw new StudentNotFoundException("Student with email " + email + " not found");
        }
        studentRepository.deleteByEmail(email);
        return true;
    }

    /**
     * Maps a StudentDTO to a Student entity.
     *
     * @param studentDTO The DTO to map.
     * @return The mapped Student entity.
     */
    private Student mapToStudent(StudentDTO studentDTO) {
        if (studentDTO == null) {
            throw new IllegalArgumentException("StudentDTO cannot be null");
        }

        Student student = new Student();

        if (studentDTO.getPerson() != null) {
            student.setEmail(studentDTO.getPerson().getEmail());
            student.setFullName(studentDTO.getPerson().getFullName());
        }

        return student;
    }

    /**
     * Maps a Student entity to a StudentDTO.
     *
     * @param student The entity to map.
     * @return The mapped StudentDTO.
     */
    private StudentDTO mapToStudentDTO(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        PersonDTO personDTO = new PersonDTO();
        personDTO.setEmail(student.getEmail());
        personDTO.setFullName(student.getFullName());
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setPerson(personDTO);
        return studentDTO;
    }
}
