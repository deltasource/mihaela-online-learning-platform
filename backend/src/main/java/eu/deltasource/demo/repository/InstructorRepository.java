package eu.deltasource.demo.repository;

import eu.deltasource.demo.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Instructor entities.
 * Extends JpaRepository to leverage Spring Data JPA functionality.
 */
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    /**
     * Retrieves an instructor by their email address.
     *
     * @param email The email address to search for
     * @return Optional containing the instructor if found, empty otherwise
     */
    @Query("SELECT i FROM Instructor i JOIN i.person p WHERE p.email = :email")
    Optional<Instructor> findByPersonEmail(@Param("email") String email);

    /**
     * Checks if an instructor exists in the database by their email.
     *
     * @param email The email to check
     * @return true if the instructor exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Instructor i JOIN i.person p WHERE p.email = :email")
    boolean existsByPersonEmail(@Param("email") String email);

    /**
     * Deletes an instructor by their person's email.
     *
     * @param email The email of the instructor to delete
     * @return The number of records deleted
     */
    @Query("DELETE FROM Instructor i WHERE i.person.email = :email")
    int deleteByPersonEmail(@Param("email") String email);
}
