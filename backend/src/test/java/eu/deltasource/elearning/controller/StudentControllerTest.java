package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.StudentDTO;
import eu.deltasource.elearning.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createStudent_givenValidStudentDTO_whenPostRequest_thenReturnCreatedStudent() throws Exception {
        // Given
        StudentDTO sampleStudentDTO = new StudentDTO();
        sampleStudentDTO.setEmail("student@example.com");
        sampleStudentDTO.setFirstName("Alice");
        sampleStudentDTO.setLastName("Smith");

        //When
        when(studentService.createStudent(any(StudentDTO.class)))
                .thenReturn(sampleStudentDTO);
        mockMvc.perform(post("/students/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStudentDTO)))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@example.com"))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    public void getStudentByEmail_givenValidEmail_whenGetRequest_thenReturnStudent() throws Exception {
        // Given
        String email = "student@example.com";
        StudentDTO sampleStudentDTO = new StudentDTO();
        sampleStudentDTO.setEmail(email);
        sampleStudentDTO.setFirstName("Alice");
        sampleStudentDTO.setLastName("Smith");

        //When
        when(studentService.getStudentByEmail(email)).thenReturn(sampleStudentDTO);
        mockMvc.perform(get("/students/v1/{email}", email))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    public void updateStudentByEmail_givenValidEmailAndValidStudentDTO_whenPutRequest_thenReturnUpdatedStudent() throws Exception {
        // Given
        String email = "student@example.com";
        StudentDTO updatedStudentDTO = new StudentDTO();
        updatedStudentDTO.setEmail(email);
        updatedStudentDTO.setFirstName("Alicia");
        updatedStudentDTO.setLastName("Johnson");

        //When
        when(studentService.updateStudentByEmail(eq(email), any(StudentDTO.class)))
                .thenReturn(updatedStudentDTO);
        mockMvc.perform(put("/students/v1/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStudentDTO)))

                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value("Alicia"))
                .andExpect(jsonPath("$.lastName").value("Johnson"));
    }

    @Test
    public void deleteStudent_givenValidEmail_whenDeleteRequest_thenReturnNoContent() throws Exception {
        // Given
        String email = "student@example.com";
        doNothing().when(studentService).deleteStudent(email);

        // When
        mockMvc.perform(delete("/students/v1/{email}", email))

                //Then
                .andExpect(status().isNoContent());
        verify(studentService, times(1)).deleteStudent(email);
    }
}
