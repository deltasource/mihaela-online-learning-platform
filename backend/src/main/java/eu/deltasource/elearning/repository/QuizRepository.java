package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {
}
