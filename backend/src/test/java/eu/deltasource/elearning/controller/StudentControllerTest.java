package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The @AutoConfigureMockMvc(addFilters = false) annotation is used in Spring Boot testing to disable the automatic addition of Spring Security filters when configuring MockMvc.
 * By default, when @AutoConfigureMockMvc is used, any registered filters (such as security filters) are applied to the MockMvc instance. Setting addFilters = false prevents these filters from being added, allowing tests to bypass security constraints and focus on controller logic without authentication or authorization requirements.
 **/
@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidStudentDTO_whenCreateStudent_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setEmail("john.doe@example.com");
        when(studentService.createStudent(any(StudentDTO.class))).thenReturn(studentDTO);

        // When
        var result = mockMvc.perform(post("/students/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)));

        // Then
        result.andExpect(status().isOk());
        verify(studentService, times(1)).createStudent(any(StudentDTO.class));
    }

    @Test
    void givenValidEmail_whenGetStudentByEmail_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        String email = "john.doe@example.com";
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setEmail(email);
        when(studentService.getStudentByEmail(email)).thenReturn(studentDTO);

        // When
        var result = mockMvc.perform(get("/students/v1/{email}", email));

        // Then
        result.andExpect(status().isOk());
        verify(studentService, times(1)).getStudentByEmail(eq(email));
    }

    @Test
    void givenValidEmailAndDTO_whenUpdateStudentByEmail_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        String email = "john.doe@example.com";
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Updated");
        studentDTO.setEmail(email);
        when(studentService.updateStudentByEmail(eq(email), any(StudentDTO.class))).thenReturn(studentDTO);

        // When
        var result = mockMvc.perform(put("/students/v1/{email}", email)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)));

        // Then
        result.andExpect(status().isOk());
        verify(studentService, times(1)).updateStudentByEmail(eq(email), any(StudentDTO.class));
    }

    @Test
    void givenValidEmail_whenDeleteStudent_thenReturnsNoContentAndServiceCalled() throws Exception {
        // Given
        String email = "john.doe@example.com";
        doNothing().when(studentService).deleteStudent(email);

        // When
        var result = mockMvc.perform(delete("/students/v1/{email}", email));

        // Then
        result.andExpect(status().isNoContent());
        verify(studentService, times(1)).deleteStudent(eq(email));
    }

    @Test
    void givenInvalidStudentDTO_whenCreateStudent_thenReturnsBadRequest() throws Exception {
        // Given
        StudentDTO studentDTO = new StudentDTO(); // missing required fields

        // When
        var result = mockMvc.perform(post("/students/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidEmailFormat_whenGetStudentByEmail_thenReturnsBadRequest() throws Exception {
        // Given
        String invalidEmail = "not-an-email";

        // When
        var result = mockMvc.perform(get("/students/v1/{email}", invalidEmail));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidEmailFormat_whenUpdateStudentByEmail_thenReturnsBadRequest() throws Exception {
        // Given
        String invalidEmail = "not-an-email";
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("John");
        studentDTO.setLastName("Doe");
        studentDTO.setEmail(invalidEmail);

        // When
        var result = mockMvc.perform(put("/students/v1/{email}", invalidEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidEmailFormat_whenDeleteStudent_thenReturnsBadRequest() throws Exception {
        // Given
        String invalidEmail = "not-an-email";

        // When
        var result = mockMvc.perform(delete("/students/v1/{email}", invalidEmail));

        // Then
        result.andExpect(status().isBadRequest());
    }
}
