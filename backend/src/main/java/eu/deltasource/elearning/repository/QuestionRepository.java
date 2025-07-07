package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
     List<Question> findByQuizId(UUID quizId);
     List<Question> findByQuizIdOrderByOrderAsc(UUID quizId);
}
