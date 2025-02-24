package eu.deltasource.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.demo.DTOs.InstructorDTO;
import eu.deltasource.demo.service.InstructorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the InstructorController class.
 * These tests verify the behavior of the InstructorController's endpoints
 * using Spring's MockMvc for simulating HTTP requests.
 */
@WebMvcTest(InstructorController.class)
public class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenValidInstructorData_whenCreatingInstructor_thenReturnCreatedInstructor() throws Exception {
        // Given
        InstructorDTO instructorDTO = InstructorDTO.builder()
                .id(1)
                .email("instructor@example.com")
                .fullName("Instructor Name")
                .salary(50000)
                .department("Computer Science")
                .build();

        when(instructorService.createInstructor(any(InstructorDTO.class))).thenReturn(instructorDTO);

        // When
        ResultActions result = mockMvc.perform(post("/instructors/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(instructorDTO)));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(instructorDTO)));
    }

    @Test
    public void givenExistingEmail_whenCreatingInstructor_thenReturnNull() throws Exception {
        // Given
        InstructorDTO instructorDTO = InstructorDTO.builder()
                .id(1)
                .email("existing@example.com")
                .fullName("Existing Instructor")
                .salary(50000)
                .department("Mathematics")
                .build();

        when(instructorService.createInstructor(any(InstructorDTO.class))).thenReturn(null);

        // When
        ResultActions result = mockMvc.perform(post("/instructors/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(instructorDTO)));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void givenExistingInstructorEmail_whenGettingInstructor_thenReturnInstructorDetails() throws Exception {
        // Given
        InstructorDTO instructorDTO = InstructorDTO.builder()
                .id(1)
                .email("get@example.com")
                .fullName("Get Instructor")
                .salary(60000)
                .department("Physics")
                .build();

        when(instructorService.getInstructorByEmail("get@example.com")).thenReturn(instructorDTO);

        // When
        ResultActions result = mockMvc.perform(get("/instructors/v1/{email}", "get@example.com"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(instructorDTO)));
    }

    @Test
    public void givenNonExistentInstructorEmail_whenGettingInstructor_thenReturnNull() throws Exception {
        // Given
        when(instructorService.getInstructorByEmail("nonexistent@example.com")).thenReturn(null);

        // When
        ResultActions result = mockMvc.perform(get("/instructors/v1/{email}", "nonexistent@example.com"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void givenExistingInstructorEmail_whenDeletingInstructor_thenReturnTrue() throws Exception {
        // Given
        when(instructorService.deleteInstructor("delete@example.com")).thenReturn(true);

        // When
        ResultActions result = mockMvc.perform(delete("/instructors/v1/{email}", "delete@example.com"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void givenNonExistentInstructorEmail_whenDeletingInstructor_thenReturnFalse() throws Exception {
        // Given
        when(instructorService.deleteInstructor("nonexistent@example.com")).thenReturn(false);

        // When
        ResultActions result = mockMvc.perform(delete("/instructors/v1/{email}", "nonexistent@example.com"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
