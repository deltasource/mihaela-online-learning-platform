package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.exception.StudentAlreadyExistsException;
import eu.deltasource.elearning.exception.StudentNotFoundException;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.StudentRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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

    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Optional<Student> existingStudent = studentRepository.findByEmail(studentDTO.getEmail());
        if (existingStudent.isPresent()) {
            throw new StudentAlreadyExistsException("Student with email " + studentDTO.getEmail() + " already exists.");
        }
        Student student = mapToStudent(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return mapToStudentDTO(savedStudent);
    }

    public Student getStudentById(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        String.format("Student with ID %s not found", studentId)));
    }

    public StudentDTO getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException("Student with email " + email + " not found"));
        return mapToStudentDTO(student);
    }

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

    @Transactional
    public void deleteStudent(String email) {
        if (!studentRepository.existsByEmail(email)) {
            throw new StudentNotFoundException("Student with email " + email + " not found");
        }
        studentRepository.deleteByEmail(email);
        if (studentRepository.existsByEmail(email)) {
            throw new StudentAlreadyExistsException("Failed to delete student with email " + email);
        }
    }

    @NotNull
    private Student mapToStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setEmail(studentDTO.getEmail());
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        return student;
    }

    @NotNull
    private StudentDTO mapToStudentDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setEmail(student.getEmail());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        return studentDTO;
    }
}
