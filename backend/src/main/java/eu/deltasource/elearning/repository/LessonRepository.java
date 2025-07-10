package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {
    List<Lesson> findByCourseId(UUID courseId);

    List<Lesson> findByCourseIdOrderByOrderAsc(UUID courseId);
}
