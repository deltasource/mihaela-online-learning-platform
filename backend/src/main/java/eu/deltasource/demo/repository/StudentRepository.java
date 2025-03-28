package eu.deltasource.demo.repository;

import eu.deltasource.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Student entities.
 * Extends JpaRepository to leverage Spring Data JPA functionality.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    /**
     * Retrieves a student by their email address.
     *
     * @param email The email address to search for
     * @return Optional containing the student if found, empty otherwise
     */
    Optional<Student> findByEmail(String email);

    /**
     * Checks if a student exists in the database by their email.
     *
     * @param email The email to check
     * @return true if the student exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Deletes a student by their email.
     *
     * @param email The email of the student to delete
     * @return The number of records deleted
     */
    void deleteByEmail(String email);
}
