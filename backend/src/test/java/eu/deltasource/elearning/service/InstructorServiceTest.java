package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.exception.InstructorAlreadyExistsException;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    private Instructor instructor;
    private InstructorDTO instructorDTO;

    @BeforeEach
    void setUp() {
        instructor = new Instructor();
        instructor.setEmail("john.doe@example.com");
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDepartment("Computer Science");

        instructorDTO = new InstructorDTO();
        instructorDTO.setEmail("john.doe@example.com");
        instructorDTO.setFirstName("John");
        instructorDTO.setLastName("Doe");
        instructorDTO.setDepartment("Computer Science");
    }

    @Test
    void createInstructor_ShouldReturnInstructorDTO() {
        // Given
        when(instructorRepository.findByEmail(instructor.getEmail())).thenReturn(Optional.empty());
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        InstructorDTO result = instructorService.createInstructor(instructorDTO);

        // Then
        assertNotNull(result);
        assertEquals(instructorDTO.getEmail(), result.getEmail());
        assertEquals(instructorDTO.getFirstName(), result.getFirstName());
        assertEquals(instructorDTO.getLastName(), result.getLastName());
        assertEquals(instructorDTO.getDepartment(), result.getDepartment());

        verify(instructorRepository).findByEmail(instructor.getEmail());
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void createInstructor_WithExistingInstructor_ShouldThrowException() {
        // Given
        when(instructorRepository.findByEmail(instructor.getEmail())).thenReturn(Optional.of(instructor));

        // When & Then
        assertThrows(InstructorAlreadyExistsException.class, () -> instructorService.createInstructor(instructorDTO));
        verify(instructorRepository).findByEmail(instructor.getEmail());
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void getInstructorByEmail_ShouldReturnInstructorDTO() {
        // Given
        when(instructorRepository.findByEmail(instructor.getEmail())).thenReturn(Optional.of(instructor));

        // When
        InstructorDTO result = instructorService.getInstructorByEmail(instructor.getEmail());

        // Then
        assertNotNull(result);
        assertEquals(instructor.getEmail(), result.getEmail());
        assertEquals(instructor.getFirstName(), result.getFirstName());
        assertEquals(instructor.getLastName(), result.getLastName());
        assertEquals(instructor.getDepartment(), result.getDepartment());

        verify(instructorRepository).findByEmail(instructor.getEmail());
    }

    @Test
    void getInstructorByEmail_WithInvalidEmail_ShouldThrowException() {
        // Given
        when(instructorRepository.findByEmail(instructor.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InstructorNotFoundException.class, () -> instructorService.getInstructorByEmail(instructor.getEmail()));
        verify(instructorRepository).findByEmail(instructor.getEmail());
    }

    @Test
    void updateInstructorByEmail_ShouldReturnUpdatedInstructorDTO() {
        // Given
        InstructorDTO updatedDTO = new InstructorDTO();
        updatedDTO.setFirstName("John");
        updatedDTO.setLastName("Doe");
        updatedDTO.setDepartment("Mathematics");

        when(instructorRepository.findByEmail(instructor.getEmail())).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        InstructorDTO result = instructorService.updateInstructorByEmail(instructor.getEmail(), updatedDTO);

        // Then
        assertNotNull(result);
        assertEquals(updatedDTO.getFirstName(), result.getFirstName());
        assertEquals(updatedDTO.getLastName(), result.getLastName());
        assertEquals(updatedDTO.getDepartment(), result.getDepartment());

        verify(instructorRepository).findByEmail(instructor.getEmail());
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void updateInstructorByEmail_WithInvalidEmail_ShouldThrowException() {
        // Given
        InstructorDTO updatedDTO = new InstructorDTO();
        updatedDTO.setFirstName("Updated John");
        updatedDTO.setLastName("Updated Doe");
        updatedDTO.setDepartment("Mathematics");

        when(instructorRepository.findByEmail(instructor.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InstructorNotFoundException.class, () -> instructorService.updateInstructorByEmail(instructor.getEmail(), updatedDTO));
        verify(instructorRepository).findByEmail(instructor.getEmail());
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void deleteInstructor_ShouldReturnTrue() {
        // Given
        when(instructorRepository.existsByEmail(instructor.getEmail())).thenReturn(true);
        when(instructorRepository.deleteByEmail(instructor.getEmail())).thenReturn(1);

        // When
        boolean result = instructorService.deleteInstructor(instructor.getEmail());

        // Then
        assertTrue(result);
        verify(instructorRepository).existsByEmail(instructor.getEmail());
        verify(instructorRepository).deleteByEmail(instructor.getEmail());
    }

    @Test
    void deleteInstructor_WithInvalidEmail_ShouldThrowException() {
        // Given
        when(instructorRepository.existsByEmail(instructor.getEmail())).thenReturn(false);

        // When & Then
        assertThrows(InstructorNotFoundException.class, () -> instructorService.deleteInstructor(instructor.getEmail()));
        verify(instructorRepository).existsByEmail(instructor.getEmail());
        verify(instructorRepository, never()).deleteByEmail(instructor.getEmail());
    }

    @Test
    void updateInstructorByEmail_WithNullEmail_ShouldThrowException() {
        // Given
        InstructorDTO updatedDTO = new InstructorDTO();
        updatedDTO.setFirstName("Updated John");
        updatedDTO.setLastName("Updated Doe");
        updatedDTO.setDepartment("Mathematics");

        // When & Then
        assertThrows(InstructorNotFoundException.class, () -> instructorService.updateInstructorByEmail(null, updatedDTO));
        verify(instructorRepository, never()).save(any(Instructor.class));
    }
}
