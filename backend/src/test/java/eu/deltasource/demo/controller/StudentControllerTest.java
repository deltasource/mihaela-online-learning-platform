package eu.deltasource.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.demo.DTOs.StudentDTO;
import eu.deltasource.demo.exception.StudentNotFoundException;
import eu.deltasource.demo.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenValidStudentData_whenCreatingStudent_thenReturnCreatedStudent() throws Exception {
        // Given
        StudentDTO studentDTO = StudentDTO.builder()
                .id(1)
                .email("test@example.com")
                .fullName("Test Student")
                .build();

        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(studentDTO);

        // When
        ResultActions result = mockMvc.perform(post("/students/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentDTO)));
    }

    @Test
    public void givenExistingEmail_whenCreatingStudent_thenReturnNull() throws Exception {
        // Given
        StudentDTO studentDTO = StudentDTO.builder()
                .id(1)
                .email("existing@example.com")
                .fullName("Existing Student")
                .build();

        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(null);

        // When
        ResultActions result = mockMvc.perform(post("/students/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void givenExistingStudentEmail_whenGettingStudent_thenReturnStudentDetails() throws Exception {
        // Given
        StudentDTO studentDTO = StudentDTO.builder()
                .id(1)
                .email("get@example.com")
                .fullName("Get Student")
                .build();

        when(studentService.getStudentByEmail("get@example.com")).thenReturn(studentDTO);

        // When
        ResultActions result = mockMvc.perform(get("/students/v1/{email}", "get@example.com"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(studentDTO)));
    }

    @Test
    public void givenNonExistentStudentEmail_whenGettingStudent_thenReturnNull() throws Exception {
        // Given
        when(studentService.getStudentByEmail("nonexistent@example.com")).thenReturn(null);

        // When
        ResultActions result = mockMvc.perform(get("/students/v1/{email}", "nonexistent@example.com"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void givenExistingStudentEmail_whenDeletingStudent_thenReturnTrue() throws Exception {
        // Given
        when(studentService.deleteStudent("delete@example.com")).thenReturn(true);

        // When
        ResultActions result = mockMvc.perform(delete("/students/v1/{email}", "delete@example.com"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void givenNonExistentStudentEmail_whenDeletingStudent_thenReturnFalse() throws Exception {
        // Given
        when(studentService.deleteStudent("nonexistent@example.com")).thenReturn(false);

        // When
        ResultActions result = mockMvc.perform(delete("/students/v1/{email}", "nonexistent@example.com"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void getStudentByEmail_NonExistentStudent_ReturnsNotFound() throws Exception {
        // Given
        String nonExistentEmail = "nonexistent@example.com";
        when(studentService.getStudentByEmail(anyString()))
                .thenThrow(new StudentNotFoundException(nonExistentEmail));

        // When
        var resultActions = mockMvc.perform(get("/students/v1/" + nonExistentEmail)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Student not found with this email: " + nonExistentEmail))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void getStudentByEmail_NullPointerException_ReturnsBadRequest() throws Exception {
        // Given
        String email = "test@example.com";
        when(studentService.getStudentByEmail(anyString()))
                .thenThrow(new NullPointerException("Simulated NullPointerException"));

        // When & Then
        mockMvc.perform(get("/students/v1/" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
