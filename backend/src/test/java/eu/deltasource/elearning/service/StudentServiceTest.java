package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.exception.StudentAlreadyExistsException;
import eu.deltasource.elearning.exception.StudentNotFoundException;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDTO studentDTO;
    private final String email = "test@student.com";
    private final UUID studentId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(studentId);
        student.setEmail(email);
        student.setFirstName("John");
        student.setLastName("Doe");

        studentDTO = new StudentDTO();
        studentDTO.setEmail(email);
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
    }

    @Test
    void createStudent_ShouldSaveAndReturnDTO_WhenEmailNotExists() {
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO result = studentService.createStudent(studentDTO);

        assertEquals(email, result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(studentRepository).findByEmail(email);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void createStudent_ShouldThrow_WhenEmailAlreadyExists() {
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));

        assertThrows(StudentAlreadyExistsException.class,
                () -> studentService.createStudent(studentDTO));

        verify(studentRepository).findByEmail(email);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void getStudentById_ShouldReturnStudent() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getId());
    }

    @Test
    void getStudentById_ShouldThrow_WhenNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.getStudentById(studentId));
    }

    @Test
    void getStudentByEmail_ShouldReturnDTO() {
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));

        StudentDTO result = studentService.getStudentByEmail(email);

        assertEquals(email, result.getEmail());
    }

    @Test
    void getStudentByEmail_ShouldThrow_WhenNotFound() {
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.getStudentByEmail(email));
    }

    @Test
    void updateStudentByEmail_ShouldUpdateAndReturnDTO() {
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO result = studentService.updateStudentByEmail(email, studentDTO);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudentByEmail_ShouldThrow_WhenNotFound() {
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.updateStudentByEmail(email, studentDTO));
    }

    @Test
    void deleteStudent_ShouldDelete_WhenExists() {
        when(studentRepository.existsByEmail(email)).thenReturn(true).thenReturn(false);

        studentService.deleteStudent(email);

        verify(studentRepository).deleteByEmail(email);
    }

    @Test
    void deleteStudent_ShouldThrow_WhenNotFoundInitially() {
        when(studentRepository.existsByEmail(email)).thenReturn(false);

        assertThrows(StudentNotFoundException.class,
                () -> studentService.deleteStudent(email));

        verify(studentRepository, never()).deleteByEmail(email);
    }

    @Test
    void deleteStudent_ShouldThrow_WhenDeleteFails() {
        when(studentRepository.existsByEmail(email)).thenReturn(true);
        when(studentRepository.existsByEmail(email)).thenReturn(true);

        doNothing().when(studentRepository).deleteByEmail(email);

        assertThrows(StudentAlreadyExistsException.class,
                () -> studentService.deleteStudent(email));
    }
}
