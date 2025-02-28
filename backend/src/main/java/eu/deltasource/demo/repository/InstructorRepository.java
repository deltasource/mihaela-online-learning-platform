package eu.deltasource.demo.repository;

import eu.deltasource.demo.model.Instructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for managing Instructor entities.
 * This class provides in-memory storage and retrieval operations for Instructor objects.
 */
@Getter
@Component
public class InstructorRepository {

    private final Map<String, Instructor> instructorDatabase = new HashMap<>();

    /**
     * Retrieves an instructor by their email address.
     *
     * @param email The email address to search for
     * @return Optional containing the instructor if found, empty otherwise
     */
    public Optional<Instructor> getByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(instructorDatabase.get(email));
    }

    /**
     * Saves an instructor to the database.
     * Uses the instructor's email (from Person object) as the key.
     *
     * @param instructor The instructor to save
     * @return The saved instructor
     * @throws IllegalArgumentException if instructor or instructor's person or email is null
     */
    public Instructor save(Instructor instructor) {
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor cannot be null");
        }
        if (instructor.getPerson() == null) {
            throw new IllegalArgumentException("Instructor's person cannot be null");
        }
        if (instructor.getPerson().getEmail() == null) {
            throw new IllegalArgumentException("Instructor's email cannot be null");
        }

        instructorDatabase.put(instructor.getPerson().getEmail(), instructor);
        return instructor;
    }

    /**
     * Removes an instructor from the database by their email.
     *
     * @param email The email of the instructor to remove
     * @return true if the instructor was removed, false if not found
     */
    public boolean remove(String email) {
        if (email == null) {
            return false;
        }
        return instructorDatabase.remove(email) != null;
    }

    /**
     * Checks if an instructor exists in the database by their email.
     *
     * @param email The email to check
     * @return true if the instructor exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        if (email == null) {
            return false;
        }
        return instructorDatabase.containsKey(email);
    }
}
