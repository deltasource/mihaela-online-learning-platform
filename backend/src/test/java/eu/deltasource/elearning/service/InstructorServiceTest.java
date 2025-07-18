package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.InstructorDTO;
import eu.deltasource.elearning.exception.InstructorAlreadyExistsException;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private InstructorDTO buildDTO(String email) {
        InstructorDTO dto = new InstructorDTO();
        dto.setEmail(email);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDepartment("Math");
        return dto;
    }

    private Instructor buildInstructor(String email) {
        Instructor instructor = new Instructor();
        instructor.setId(UUID.randomUUID());
        instructor.setEmail(email);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDepartment("Math");
        return instructor;
    }

    @Test
    void createInstructor_success() {
        // Given
        String email = "john.doe@example.com";
        InstructorDTO dto = buildDTO(email);
        Instructor instructor = buildInstructor(email);
        when(instructorRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        InstructorDTO result = instructorService.createInstructor(dto);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(instructorRepository).findByEmail(email);
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void createInstructor_alreadyExists_throws() {
        // Given
        String email = "john.doe@example.com";
        InstructorDTO dto = buildDTO(email);
        Instructor instructor = buildInstructor(email);

        // When
        when(instructorRepository.findByEmail(email)).thenReturn(Optional.of(instructor));

        // Then
        assertThrows(InstructorAlreadyExistsException.class, () -> instructorService.createInstructor(dto));
        verify(instructorRepository).findByEmail(email);
        verify(instructorRepository, never()).save(any());
    }

    @Test
    void getInstructorByEmail_success() {
        // Given
        String email = "john.doe@example.com";
        Instructor instructor = buildInstructor(email);
        when(instructorRepository.findByEmail(email)).thenReturn(Optional.of(instructor));

        // When
        InstructorDTO result = instructorService.getInstructorByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(instructorRepository).findByEmail(email);
    }

    @Test
    void getInstructorByEmail_notFound_throws() {
        //Given
        String email = "notfound@example.com";
        when(instructorRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InstructorNotFoundException.class, () -> instructorService.getInstructorByEmail(email));
        verify(instructorRepository).findByEmail(email);
    }

    @Test
    void updateInstructorByEmail_success_withEmailNotNull() {
        // Given
        String email = "john.doe@example.com";
        InstructorDTO dto = buildDTO(email);
        Instructor instructor = buildInstructor(email);
        when(instructorRepository.findByEmail(email)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        InstructorDTO result = instructorService.updateInstructorByEmail(email, dto);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(instructorRepository).findByEmail(email);
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void updateInstructorByEmail_notFound_throws() {
        // Given
        String email = "notfound@example.com";
        InstructorDTO dto = buildDTO(email);

        // When
        when(instructorRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Then
        assertThrows(InstructorNotFoundException.class, () -> instructorService.updateInstructorByEmail(email, dto));
        verify(instructorRepository).findByEmail(email);
        verify(instructorRepository, never()).save(any());
    }

    @Test
    void updateInstructorByEmail_withNullEmailInDTO_onlyDepartmentUpdated() {
        // Given
        String email = "john.doe@example.com";
        InstructorDTO dto = buildDTO(email);
        dto.setEmail(null);
        Instructor instructor = buildInstructor(email);
        when(instructorRepository.findByEmail(email)).thenReturn(Optional.of(instructor));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // When
        InstructorDTO result = instructorService.updateInstructorByEmail(email, dto);

        // Then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(instructorRepository).findByEmail(email);
        verify(instructorRepository).save(any(Instructor.class));
    }

    @Test
    void deleteInstructor_success() {
        // Given
        String email = "john.doe@example.com";
        when(instructorRepository.existsByEmail(email)).thenReturn(true);
        when(instructorRepository.deleteByEmail(email)).thenReturn(1);

        // When & Then
        assertDoesNotThrow(() -> instructorService.deleteInstructor(email));
        verify(instructorRepository).existsByEmail(email);
        verify(instructorRepository).deleteByEmail(email);
    }

    @Test
    void deleteInstructor_notFoundByExists_throws() {
        // Given
        String email = "notfound@example.com";
        when(instructorRepository.existsByEmail(email)).thenReturn(false);

        // When & Then
        assertThrows(InstructorNotFoundException.class, () -> instructorService.deleteInstructor(email));
        verify(instructorRepository).existsByEmail(email);
        verify(instructorRepository, never()).deleteByEmail(email);
    }

    @Test
    void deleteInstructor_notFoundByDelete_throws() {
        // Given
        String email = "john.doe@example.com";
        when(instructorRepository.existsByEmail(email)).thenReturn(true);
        when(instructorRepository.deleteByEmail(email)).thenReturn(0);

        // When & Then
        assertThrows(InstructorNotFoundException.class, () -> instructorService.deleteInstructor(email));
        verify(instructorRepository).existsByEmail(email);
        verify(instructorRepository).deleteByEmail(email);
    }
}
