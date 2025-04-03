package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void givenNewStudent_whenCreatingStudent_thenStudentIsCreatedSuccessfully() {
        // Given
        StudentDTO newStudent = new StudentDTO(1, "test@example.com", "Test Student");
        Student savedStudent = new Student(1, "test@example.com", "Test Student");

        // When
        StudentDTO result = studentService.createStudent(newStudent);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(newStudent.getId());
        assertThat(result.getEmail()).isEqualTo(newStudent.getEmail());
        assertThat(result.getFullName()).isEqualTo(newStudent.getFullName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void givenExistingStudent_whenGettingStudentByEmail_thenCorrectStudentIsReturned() {
        // Given
        String email = "test@example.com";
        Student existingStudent = new Student(1, email, "Test Student");
        when(studentRepository.getByEmail(email)).thenReturn(Optional.of(existingStudent));

        // When
        StudentDTO result = studentService.getStudentByEmail(email);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingStudent.getId());
        assertThat(result.getEmail()).isEqualTo(existingStudent.getEmail());
        assertThat(result.getFullName()).isEqualTo(existingStudent.getFullName());
        verify(studentRepository, times(1)).getByEmail(email);
    }

    @Test
    void givenNonExistentStudent_whenGettingStudentByEmail_thenExceptionIsThrown() {
        // Given
        String nonExistentEmail = "nonexistent@example.com";
        when(studentRepository.getByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> studentService.getStudentByEmail(nonExistentEmail))
                .isInstanceOf(RuntimeException.class);
        verify(studentRepository, times(1)).getByEmail(nonExistentEmail);
    }

    @Test
    void givenExistingStudent_whenDeletingStudent_thenDeletionSucceeds() {
        // Given
        String email = "test@example.com";
        when(studentRepository.remove(email)).thenReturn(true);

        // When
        boolean result = studentService.deleteStudent(email);

        // Then
        assertThat(result).isTrue();
        verify(studentRepository, times(1)).remove(email);
    }

    @Test
    void givenNonExistentEmail_whenDeletingStudent_thenDeletionFails() {
        // Given
        String nonExistentEmail = "nonexistent@example.com";
        when(studentRepository.remove(nonExistentEmail)).thenReturn(false);

        // When
        boolean result = studentService.deleteStudent(nonExistentEmail);

        // Then
        assertThat(result).isFalse();
        verify(studentRepository, times(1)).remove(nonExistentEmail);
    }
}
