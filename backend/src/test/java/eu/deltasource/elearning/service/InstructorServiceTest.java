package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    @Test
    void givenExistingEmail_whenGetInstructorByEmail_thenReturnsInstructorDTO() {
        // Given
        String email = "eray.ali@example.com";
        Instructor instructor = new Instructor();
        instructor.setId(UUID.randomUUID());
        instructor.setEmail(email);
        instructor.setFirstName("Eray");
        instructor.setLastName("Ali");
        instructor.setDepartment("Software Engineering");

        when(instructorRepository.findByEmail(email)).thenReturn(Optional.of(instructor));

        // When
        InstructorDTO result = instructorService.getInstructorByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(instructor.getEmail(), result.getEmail());
        assertEquals(instructor.getFirstName(), result.getFirstName());
        verify(instructorRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenNonExistentEmail_whenGetInstructorByEmail_thenThrowsInstructorNotFoundException() {
        // Given
        String email = "not.found@example.com";
        when(instructorRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Executable getInstructorAction = () -> instructorService.getInstructorByEmail(email);

        // Then
        assertThrows(InstructorNotFoundException.class, getInstructorAction);
        verify(instructorRepository, times(1)).findByEmail(email);
    }
}
