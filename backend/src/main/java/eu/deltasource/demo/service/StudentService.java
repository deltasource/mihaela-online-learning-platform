package eu.deltasource.demo.service;

import eu.deltasource.demo.DTOs.StudentDTO;
import eu.deltasource.demo.exception.StudentNotFoundException;
import eu.deltasource.demo.model.Student;
import eu.deltasource.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing student-related operations.
 * This class acts as an intermediary between the controller and the repository,
 * handling business logic for student management.
 */
@RequiredArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = new Student(studentDTO.getId(), studentDTO.getEmail(), studentDTO.getFullName());
        studentRepository.save(student);
        return new StudentDTO(student.getId(), student.getEmail(), student.getFullName());
    }

    public StudentDTO getStudentByEmail(String email) {
        Optional<Student> optionalStudent = studentRepository.getByEmail(email);

        if (optionalStudent.isEmpty()) {
            throw new StudentNotFoundException(email);
        }
        Student student = optionalStudent.get();
        return new StudentDTO(student.getId(), student.getEmail(), student.getFullName());
    }

    public boolean deleteStudent(String email) {
        return studentRepository.remove(email);
    }
}
