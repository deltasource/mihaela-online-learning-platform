package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    List<Quiz> findByCourseId(UUID courseId);
    List<Quiz> findByCourseIdOrderByOrderAsc(UUID courseId);
}
