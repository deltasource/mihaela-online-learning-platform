package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.service.InstructorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorControllerTest {

    @Mock
    private InstructorService instructorService;

    @InjectMocks
    private InstructorController instructorController;

    @Test
    void givenValidInstructorDTO_whenCreateInstructor_thenReturnInstructorDTO() {
        // Given
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setFirstName("John");
        instructorDTO.setLastName("Doe");
        instructorDTO.setEmail("john.doe@example.com");

        when(instructorService.createInstructor(instructorDTO)).thenReturn(instructorDTO);

        // When
        InstructorDTO response = instructorController.createInstructor(instructorDTO);

        // Then
        assertEquals(instructorDTO, response);
        verify(instructorService, times(1)).createInstructor(instructorDTO);
    }

    @Test
    void givenValidEmail_whenGetInstructorByEmail_thenReturnInstructorDTO() {
        // Given
        String email = "john.doe@example.com";
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setEmail(email);

        when(instructorService.getInstructorByEmail(email)).thenReturn(instructorDTO);

        // When
        InstructorDTO response = instructorController.getInstructorByEmail(email);

        // Then
        assertEquals(instructorDTO, response);
        verify(instructorService, times(1)).getInstructorByEmail(email);
    }

    @Test
    void givenValidEmailAndInstructorDTO_whenUpdateInstructorByEmail_thenReturnUpdatedInstructorDTO() {
        // Given
        String email = "john.doe@example.com";
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setFirstName("John");
        instructorDTO.setLastName("Updated");
        instructorDTO.setEmail(email);

        when(instructorService.updateInstructorByEmail(email, instructorDTO)).thenReturn(instructorDTO);

        // When
        InstructorDTO response = instructorController.updateInstructorByEmail(email, instructorDTO);

        // Then
        assertEquals(instructorDTO, response);
        verify(instructorService, times(1)).updateInstructorByEmail(email, instructorDTO);
    }

    @Test
    void givenValidEmail_whenDeleteInstructor_thenVerifyServiceCalled() {
        // Given
        String email = "john.doe@example.com";

        doNothing().when(instructorService).deleteInstructor(email);

        // When
        instructorController.deleteInstructor(email);

        // Then
        verify(instructorService, times(1)).deleteInstructor(email);
    }
}
