package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    List<Lesson> findBySectionIdOrderByOrder(UUID sectionId);
}
