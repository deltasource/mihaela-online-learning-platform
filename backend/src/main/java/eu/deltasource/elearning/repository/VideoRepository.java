package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
    List<Video> findByCourseId(UUID courseId);
}
