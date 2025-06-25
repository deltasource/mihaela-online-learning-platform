package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {


}
