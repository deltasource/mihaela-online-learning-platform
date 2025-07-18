package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void givenValidStudentData_whenCreateStudent_thenStudentIsSuccessfullyCreated() {
        // Given
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setLastName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setEmail("john.doe@example.com");
        when(studentService.createStudent(studentDTO)).thenReturn(studentDTO);

        // When
        StudentDTO response = studentController.createStudent(studentDTO);

        // Then
        assertEquals(studentDTO, response);
        verify(studentService, times(1)).createStudent(studentDTO);
    }

    @Test
    void givenValidEmail_whenGetStudentByEmail_thenCorrectStudentIsReturned() {
        // Given
        String email = "john.doe@example.com";
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setEmail(email);
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        when(studentService.getStudentByEmail(email)).thenReturn(studentDTO);

        // When
        StudentDTO response = studentController.getStudentByEmail(email);

        // Then
        assertEquals(studentDTO, response);
        verify(studentService, times(1)).getStudentByEmail(email);
    }

    @Test
    void givenValidEmailAndStudentData_whenUpdateStudentByEmail_thenStudentIsUpdatedSuccessfully() {
        // Given
        String email = "john.doe@example.com";
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Updated");
        studentDTO.setEmail(email);
        when(studentService.updateStudentByEmail(email, studentDTO)).thenReturn(studentDTO);

        // When
        StudentDTO response = studentController.updateStudentByEmail(email, studentDTO);

        // Then
        assertEquals(studentDTO, response);
        verify(studentService, times(1)).updateStudentByEmail(email, studentDTO);
    }

    @Test
    void givenValidEmail_whenDeleteStudent_thenVerifyServiceCalled() {
        // Given
        String email = "john.doe@example.com";
        doNothing().when(studentService).deleteStudent(email);

        // When
        studentController.deleteStudent(email);

        // Then
        verify(studentService, times(1)).deleteStudent(email);
    }
}
