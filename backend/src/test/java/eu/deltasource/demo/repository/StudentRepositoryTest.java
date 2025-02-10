package eu.deltasource.demo.repository;

import eu.deltasource.demo.model.Student;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StudentRepositoryTest {

    private final StudentRepository studentRepository = new StudentRepository();

    @Test
    void givenNewStudent_whenSaving_thenStudentIsSavedSuccessfully() {
        // Given
        Student newStudent = new Student(1, "test@example.com", "Test Student");

        // When
        studentRepository.save(newStudent);

        // Then
        Optional<Student> savedStudent = studentRepository.getByEmail("test@example.com");
        assertThat(savedStudent).isPresent();
        assertThat(savedStudent.get()).usingRecursiveComparison().isEqualTo(newStudent);
    }

    @Test
    void givenExistingStudent_whenGettingByEmail_thenCorrectStudentIsReturned() {
        // Given
        Student student = new Student(1, "existing@example.com", "Existing Student");
        studentRepository.save(student);

        // When
        Optional<Student> result = studentRepository.getByEmail("existing@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(student);
    }

    @Test
    void givenNonExistentEmail_whenGettingByEmail_thenEmptyOptionalIsReturned() {
        // When
        Optional<Student> result = studentRepository.getByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void givenExistingStudent_whenRemoving_thenRemovalSucceeds() {
        // Given
        Student student = new Student(1, "remove@example.com", "Remove Student");
        studentRepository.save(student);

        // When
        boolean result = studentRepository.remove("remove@example.com");

        // Then
        assertThat(result).isTrue();
        assertThat(studentRepository.getByEmail("remove@example.com")).isEmpty();
    }

    @Test
    void givenNonExistentEmail_whenRemoving_thenRemovalFails() {
        // When
        boolean result = studentRepository.remove("nonexistent@example.com");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void givenMultipleStudents_whenGettingStudentDatabase_thenAllStudentsAreReturned() {
        // Given
        Student student1 = new Student(1, "student1@example.com", "Student One");
        Student student2 = new Student(2, "student2@example.com", "Student Two");
        studentRepository.save(student1);
        studentRepository.save(student2);

        // When
        Map<String, Student> studentDatabase = studentRepository.getStudentDatabase();

        // Then
        assertThat(studentDatabase).hasSize(2);
        assertThat(studentDatabase).containsKeys("student1@example.com", "student2@example.com");
        assertThat(studentDatabase.get("student1@example.com")).usingRecursiveComparison().isEqualTo(student1);
        assertThat(studentDatabase.get("student2@example.com")).usingRecursiveComparison().isEqualTo(student2);
    }
}
