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

/**
 * The @AutoConfigureMockMvc(addFilters = false) annotation is used in Spring Boot testing to disable the automatic addition of Spring Security filters when configuring MockMvc.
 * By default, when @AutoConfigureMockMvc is used, any registered filters (such as security filters) are applied to the MockMvc instance. Setting addFilters = false prevents these filters from being added, allowing tests to bypass security constraints and focus on controller logic without authentication or authorization requirements.
 **/
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
