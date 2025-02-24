package eu.deltasource.demo.service;

import eu.deltasource.demo.DTOs.InstructorDTO;
import eu.deltasource.demo.model.Instructor;
import eu.deltasource.demo.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    @Test
    void givenNewInstructor_whenCreatingInstructor_thenInstructorIsCreatedSuccessfully() {
        // Given
        InstructorDTO newInstructor = new InstructorDTO(1, "instructor@example.com", "Test Instructor", "science",5000.0);
        Instructor savedInstructor = new Instructor(1, "instructor@example.com", "Test Instructor", "science", 5000.0);

        // When
        InstructorDTO result = instructorService.createInstructor(newInstructor);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(newInstructor.getId());
        assertThat(result.getEmail()).isEqualTo(newInstructor.getEmail());
        assertThat(result.getFullName()).isEqualTo(newInstructor.getFullName());
        assertThat(result.getSalary()).isEqualTo(newInstructor.getSalary());
        assertThat(result.getDepartment()).isEqualTo(newInstructor.getDepartment());
        verify(instructorRepository, times(1)).save(any(Instructor.class));
    }

    @Test
    void givenExistingInstructor_whenGettingInstructorByEmail_thenCorrectInstructorIsReturned() {
        // Given
        String email = "instructor@example.com";
        Instructor existingInstructor = new Instructor(1, email, "Test Instructor", "science", 5000.0);
        when(instructorRepository.getByEmail(email)).thenReturn(Optional.of(existingInstructor));

        // When
        InstructorDTO result = instructorService.getInstructorByEmail(email);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingInstructor.getId());
        assertThat(result.getEmail()).isEqualTo(existingInstructor.getEmail());
        assertThat(result.getFullName()).isEqualTo(existingInstructor.getFullName());
        assertThat(result.getSalary()).isEqualTo(existingInstructor.getSalary());
        assertThat(result.getDepartment()).isEqualTo(existingInstructor.getDepartment());
        verify(instructorRepository, times(1)).getByEmail(email);
    }

    @Test
    void givenNonExistentInstructor_whenGettingInstructorByEmail_thenExceptionIsThrown() {
        // Given
        String nonExistentEmail = "nonexistentinstructor@example.com";
        when(instructorRepository.getByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> instructorService.getInstructorByEmail(nonExistentEmail))
                .isInstanceOf(RuntimeException.class);
        verify(instructorRepository, times(1)).getByEmail(nonExistentEmail);
    }

    @Test
    void givenExistingInstructor_whenDeletingInstructor_thenDeletionSucceeds() {
        // Given
        String email = "instructor@example.com";
        when(instructorRepository.remove(email)).thenReturn(true);

        // When
        boolean result = instructorService.deleteInstructor(email);

        // Then
        assertThat(result).isTrue();
        verify(instructorRepository, times(1)).remove(email);
    }

    @Test
    void givenNonExistentEmail_whenDeletingInstructor_thenDeletionFails() {
        // Given
        String nonExistentEmail = "nonexistentinstructor@example.com";
        when(instructorRepository.remove(nonExistentEmail)).thenReturn(false);

        // When
        boolean result = instructorService.deleteInstructor(nonExistentEmail);

        // Then
        assertThat(result).isFalse();
        verify(instructorRepository, times(1)).remove(nonExistentEmail);
    }
}
