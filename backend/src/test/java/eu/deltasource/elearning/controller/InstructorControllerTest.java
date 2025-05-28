package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.service.InstructorService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InstructorController.class)
public class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createInstructor_givenValidRequest_thenReturnInstructorDTO() throws Exception {
        // Given
        InstructorDTO instructor = new InstructorDTO();
        instructor.setEmail("instructor@example.com");
        instructor.setFirstName("John");
        instructor.setLastName("Doe");

        // When
        when(instructorService.createInstructor(any(InstructorDTO.class))).thenReturn(instructor);

        // Then
        mockMvc.perform(post("/instructors/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instructor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("instructor@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void createInstructor_givenInvalidEmail_thenBadRequest() throws Exception {
        // Given
        InstructorDTO instructor = new InstructorDTO();
        instructor.setEmail("invalid-email");
        instructor.setFirstName("John");
        instructor.setLastName("Doe");

        // When
        when(instructorService.createInstructor(any(InstructorDTO.class)))
                .thenThrow(new ConstraintViolationException("Invalid email format", null));

        // Then
        mockMvc.perform(post("/instructors/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instructor)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getInstructorByEmail_givenValidEmail_thenReturnInstructorDTO() throws Exception {
        // Given
        String email = "instructor@example.com";
        InstructorDTO instructor = new InstructorDTO();
        instructor.setEmail(email);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");

        // When
        when(instructorService.getInstructorByEmail(email)).thenReturn(instructor);

        // Then
        mockMvc.perform(get("/instructors/v1/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void getInstructorByEmail_givenNonExistingEmail_thenNotFound() throws Exception {
        // Given
        String email = "nonexistent@example.com";

        // When
        when(instructorService.getInstructorByEmail(email))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor not found"));

        // Then
        mockMvc.perform(get("/instructors/v1/{email}", email))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateInstructorByEmail_givenValidRequest_thenReturnUpdatedInstructorDTO() throws Exception {
        String email = "instructor@example.com";
        InstructorDTO updatedInstructor = new InstructorDTO();
        updatedInstructor.setEmail(email);
        updatedInstructor.setFirstName("Jane");
        updatedInstructor.setLastName("Smith");

        when(instructorService.updateInstructorByEmail(eq(email), any(InstructorDTO.class)))
                .thenReturn(updatedInstructor);

        mockMvc.perform(put("/instructors/v1/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInstructor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    public void updateInstructorByEmail_givenInvalidEmail_thenBadRequest() throws Exception {
        // Given
        String email = "invalid-email";
        InstructorDTO updatedInstructor = new InstructorDTO();
        updatedInstructor.setEmail(email);
        updatedInstructor.setFirstName("Jane");
        updatedInstructor.setLastName("Smith");

        // When
        when(instructorService.updateInstructorByEmail(eq(email), any(InstructorDTO.class)))
                .thenThrow(new ConstraintViolationException("Invalid email format", null));

        // Then
        mockMvc.perform(put("/instructors/v1/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInstructor)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteInstructor_givenValidEmail_thenReturnNoContent() throws Exception {
        // Given
        String email = "instructor@example.com";
        doNothing().when(instructorService).deleteInstructor(email);

        // When
        mockMvc.perform(delete("/instructors/v1/{email}", email))
                .andExpect(status().isNoContent());

        // Then
        verify(instructorService, times(1)).deleteInstructor(email);
    }

    @Test
    public void deleteInstructor_givenNonExistingEmail_thenNotFound() throws Exception {
        // Given
        String email = "nonexistent@example.com";

        // When
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor not found"))
                .when(instructorService).deleteInstructor(email);

        // Then
        mockMvc.perform(delete("/instructors/v1/{email}", email))
                .andExpect(status().isNotFound());
    }
}
