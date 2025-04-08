package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.exception.StudentNotFoundException;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Student student = mapToStudent(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return mapToStudentDTO(savedStudent);
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
    public boolean deleteStudent(String email) {
        if (!studentRepository.existsByEmail(email)) {
            throw new StudentNotFoundException("Student with email " + email + " not found");
        }
        studentRepository.deleteByEmail(email);
        return true;
    }

    private Student mapToStudent(StudentDTO studentDTO) {
        if (studentDTO == null) {
            throw new IllegalArgumentException("StudentDTO cannot be null");
        }
        Student student = new Student();
        student.setEmail(studentDTO.getEmail());
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        return student;
    }

    private StudentDTO mapToStudentDTO(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setEmail(student.getEmail());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        return studentDTO;
    }
}
