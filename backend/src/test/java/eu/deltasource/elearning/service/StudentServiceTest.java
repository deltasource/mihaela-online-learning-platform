package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.StudentAlreadyExistsException;
import eu.deltasource.elearning.exception.StudentNotFoundException;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void givenNewStudent_whenCreateStudent_thenSavesAndReturnsDTO() {
        // Given
        StudentDTO dto = new StudentDTO();
        dto.setEmail("test@mail.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        when(studentRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        Student saved = new Student();
        saved.setId(UUID.randomUUID());
        saved.setEmail(dto.getEmail());
        saved.setFirstName(dto.getFirstName());
        saved.setLastName(dto.getLastName());
        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        // When
        StudentDTO result = studentService.createStudent(dto);

        // Then
        assertEquals(dto.getEmail(), result.getFirstName());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void givenExistingEmail_whenCreateStudent_thenThrowsStudentAlreadyExistsException() {
        // Given
        StudentDTO dto = new StudentDTO();
        dto.setEmail("test@mail.com");
        when(studentRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new Student()));

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> studentService.createStudent(dto));
    }

    @Test
    void givenExistingId_whenGetStudentById_thenReturnsStudent() {
        // Given
        UUID id = UUID.randomUUID();
        Student student = new Student();
        student.setEmail("emf.@jn.com");
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        // When
        Student result = studentService.getStudentById(id);

        // Then
        assertEquals(id, result.getId());
    }

    @Test
    void givenNonExistentId_whenGetStudentById_thenThrowsStudentNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> studentService.getStudentById(id));
    }

    @Test
    void givenExistingEmail_whenGetStudentByEmail_thenReturnsDTO() {
        // Given
        String email = "test@mail.com";
        Student student = new Student();
        student.setEmail(email);
        student.setFirstName("Jane");
        student.setLastName("Smith");
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));

        // When
        StudentDTO dto = studentService.getStudentByEmail(email);

        // Then
        assertEquals(email, dto.getFirstName());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
    }

    @Test
    void givenNonExistentEmail_whenGetStudentByEmail_thenThrowsStudentNotFoundException() {
        // Given
        String email = "notfound@mail.com";
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> studentService.getStudentByEmail(email));
    }

    @Test
    void givenExistingEmail_whenUpdateStudentByEmail_thenUpdatesAndReturnsDTO() {
        // Given
        String email = "test@mail.com";
        Student existing = new Student();
        existing.setId(UUID.randomUUID());
        existing.setEmail(email);
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(existing));
        Student updated = new Student();
        updated.setId(existing.getId());
        updated.setEmail(email);
        updated.setFirstName("New");
        updated.setLastName("Name");
        when(studentRepository.save(any(Student.class))).thenReturn(updated);
        StudentDTO dto = new StudentDTO();
        dto.setEmail(email);
        dto.setFirstName("New");
        dto.setLastName("Name");

        // When
        StudentDTO result = studentService.updateStudentByEmail(email, dto);

        // Then
        assertEquals("New", result.getFirstName());
        assertEquals("Name", result.getLastName());
        assertEquals(email, result.getEmail());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void givenNonExistentEmail_whenUpdateStudentByEmail_thenThrowsStudentNotFoundException() {
        // Given
        String email = "notfound@mail.com";
        StudentDTO dto = new StudentDTO();
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(StudentNotFoundException.class, () -> studentService.updateStudentByEmail(email, dto));
    }

    @Test
    void givenExistingEmail_whenDeleteStudent_thenDeletesStudent() {
        // Given
        String email = "test@mail.com";
        when(studentRepository.existsByEmail(email)).thenReturn(true).thenReturn(false);

        // When
        studentService.deleteStudent(email);

        // Then
        verify(studentRepository).deleteByEmail(email);
    }

    @Test
    void givenNonExistentEmail_whenDeleteStudent_thenThrowsStudentNotFoundException() {
        // Given
        String email = "notfound@mail.com";
        when(studentRepository.existsByEmail(email)).thenReturn(false);

        // When & Then
        assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent(email));
    }

    @Test
    void givenDeleteFails_whenDeleteStudent_thenThrowsStudentAlreadyExistsException() {
        // Given
        String email = "fail@mail.com";
        when(studentRepository.existsByEmail(email)).thenReturn(true, true);

        // When & Then
        assertThrows(StudentAlreadyExistsException.class, () -> studentService.deleteStudent(email));
    }
}
