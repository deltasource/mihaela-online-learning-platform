package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

}
