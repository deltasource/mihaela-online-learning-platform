package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.WatchedVideos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WatchedVideosRepository extends JpaRepository<WatchedVideos, UUID> {

}
