package eu.deltasource.demo.repository;

import eu.deltasource.demo.model.Student;
import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for managing Student entities.
 * This class provides in-memory storage and retrieval operations for Student objects.
 */
@Getter
@Component
public class StudentRepository {

    private final Map<String, Student> studentDatabase = new HashMap<>();

    /**
     * Retrieves a student by their email address.
     *
     * @param email The email address to search for
     * @return Optional containing the student if found, empty otherwise
     */
    public Optional<Student> getByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(studentDatabase.get(email));
    }

    /**
     * Saves a student to the database.
     * Uses the student's email as the key.
     *
     * @param student The student to save
     * @return The saved student
     * @throws IllegalArgumentException if student or student's email is null
     */
    public Student save(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (student.getEmail() == null) {
            throw new IllegalArgumentException("Student's email cannot be null");
        }
        studentDatabase.put(student.getEmail(), student);
        return student;
    }

    /**
     * Removes a student from the database by their email.
     *
     * @param email The email of the student to remove
     * @return true if the student was removed, false if not found
     */
    public boolean remove(String email) {
        if (email == null) {
            return false;
        }
        return studentDatabase.remove(email) != null;
    }

    /**
     * Checks if a student exists in the database by their email.
     *
     * @param email The email to check
     * @return true if the student exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        if (email == null) {
            return false;
        }
        return studentDatabase.containsKey(email);
    }

    /**
     * Updates an existing student in the database.
     *
     * @param student The student to update
     * @return The updated student
     * @throws IllegalArgumentException if student doesn't exist
     */
    public Student update(Student student) {
        if (student == null || student.getEmail() == null) {
            throw new IllegalArgumentException("Invalid student data");
        }

        String email = student.getEmail();
        if (!studentDatabase.containsKey(email)) {
            throw new IllegalArgumentException("Student with email " + email + " not found");
        }

        return studentDatabase.put(email, student);
    }

    /**
     * Clears all students from the database.
     */
    public void clear() {
        studentDatabase.clear();
    }
}
