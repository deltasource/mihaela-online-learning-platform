package eu.deltasource.demo.repository;

import eu.deltasource.demo.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Instructor entities.
 */
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    Optional<Instructor> findByPersonEmail(String email);

    boolean existsByPersonEmail(String email);

    void deleteByPersonEmail(String email);
}
