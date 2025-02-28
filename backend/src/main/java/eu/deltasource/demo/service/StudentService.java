package eu.deltasource.demo.service;

import eu.deltasource.demo.DTOs.PersonDTO;
import eu.deltasource.demo.DTOs.StudentDTO;
import eu.deltasource.demo.exception.StudentNotFoundException;
import eu.deltasource.demo.model.Person;
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

        Person person = new Person();
        person.setId(studentDTO.getPerson().getId());
        person.setEmail(studentDTO.getPerson().getEmail());
        person.setFullName(studentDTO.getPerson().getFullName());

        Student student = new Student();
        student.setPerson(person);
        student.setStudentNumber(studentDTO.getStudentNumber());

        Student savedStudent = studentRepository.save(student);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(savedStudent.getPerson().getId());
        personDTO.setEmail(savedStudent.getPerson().getEmail());
        personDTO.setFullName(savedStudent.getPerson().getFullName());

        StudentDTO responseDTO = new StudentDTO();
        responseDTO.setPerson(personDTO);
        responseDTO.setStudentNumber(savedStudent.getStudentNumber());

        return responseDTO;
    }

    public StudentDTO getStudentByEmail(String email) {
        Optional<Student> optionalStudent = studentRepository.getByEmail(email);

        if (optionalStudent.isEmpty()) {
            throw new StudentNotFoundException(email);
        }

        Student student = optionalStudent.get();

        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(student.getPerson().getId());
        personDTO.setEmail(student.getPerson().getEmail());
        personDTO.setFullName(student.getPerson().getFullName());

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setPerson(personDTO);
        studentDTO.setStudentNumber(student.getStudentNumber());

        return studentDTO;
    }

    public boolean deleteStudent(String email) {
        return studentRepository.remove(email);
    }
}
