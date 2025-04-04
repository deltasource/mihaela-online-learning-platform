package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Instructor entities.
 */
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    Optional<Instructor> findByEmail(String email);

    boolean existsByEmail(String email);

    int deleteByEmail(String email);
}
