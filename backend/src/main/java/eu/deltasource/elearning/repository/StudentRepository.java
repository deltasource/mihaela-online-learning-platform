package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Student entities.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
