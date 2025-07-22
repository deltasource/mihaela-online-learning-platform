package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.service.InstructorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InstructorController.class)
@AutoConfigureMockMvc(addFilters = false)
class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidEmail_whenGetInstructorByEmail_thenReturnsInstructorDTO() throws Exception {
        // Given
        String email = "john.doe@example.com";
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setFirstName("John");
        instructorDTO.setLastName("Doe");
        instructorDTO.setEmail(email);
        when(instructorService.getInstructorByEmail(email)).thenReturn(instructorDTO);

        // When
        var result = mockMvc.perform(get("/instructors/v1/{email}", email));

        // Then
        result.andExpect(status().isOk());
        verify(instructorService, times(1)).getInstructorByEmail(email);
    }

    @Test
    void givenValidEmail_whenDeleteInstructor_thenReturnsNoContent() throws Exception {
        // Given
        String email = "john.doe@example.com";
        doNothing().when(instructorService).deleteInstructor(email);

        // When
        var result = mockMvc.perform(delete("/instructors/v1/{email}", email));

        // Then
        result.andExpect(status().isNoContent());
        verify(instructorService, times(1)).deleteInstructor(email);
    }
}
