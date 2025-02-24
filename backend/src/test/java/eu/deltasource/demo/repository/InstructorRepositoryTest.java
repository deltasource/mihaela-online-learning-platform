package eu.deltasource.demo.repository;

import eu.deltasource.demo.model.Instructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InstructorRepositoryTest {

    private InstructorRepository instructorRepository;

    @BeforeEach
    void setUp() {
        instructorRepository = new InstructorRepository();
    }

    @Test
    void givenInstructor_whenSavingInstructor_thenInstructorIsSavedSuccessfully() {
        // Given
        Instructor instructor = new Instructor(1, "instructor@example.com", "John Doe", "computer science",5000.0);

        // When
        instructorRepository.save(instructor);

        // Then
        assertThat(instructorRepository.getByEmail("instructor@example.com")).isPresent();
        assertThat(instructorRepository.getByEmail("instructor@example.com").get().getFullName()).isEqualTo("John Doe");
    }

    @Test
    void givenExistingInstructor_whenRetrievingByEmail_thenInstructorIsReturned() {
        // Given
        Instructor instructor = new Instructor(1, "instructor@example.com", "John Doe", "computer science", 5000.0);
        instructorRepository.save(instructor);

        // When
        Optional<Instructor> result = instructorRepository.getByEmail("instructor@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("instructor@example.com");
        assertThat(result.get().getFullName()).isEqualTo("John Doe");
    }

    @Test
    void givenNonExistingInstructor_whenRetrievingByEmail_thenEmptyOptionalIsReturned() {
        // When
        Optional<Instructor> result = instructorRepository.getByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isNotPresent();
    }

    @Test
    void givenInstructor_whenRemovingInstructor_thenInstructorIsRemoved() {
        // Given
        Instructor instructor = new Instructor(1, "instructor@example.com", "John Doe", "computer science", 5000.0);
        instructorRepository.save(instructor);

        // When
        boolean removed = instructorRepository.remove("instructor@example.com");

        // Then
        assertThat(removed).isTrue();
        assertThat(instructorRepository.getByEmail("instructor@example.com")).isNotPresent();
    }

    @Test
    void givenNonExistingInstructor_whenRemovingInstructor_thenReturnFalse() {
        // When
        boolean removed = instructorRepository.remove("nonexistent@example.com");

        // Then
        assertThat(removed).isFalse();
    }
}
