package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    List<Course> findByInstructorId(UUID instructorId);

    @Query("SELECT c FROM Course c WHERE c.instructor.email = :email")
    List<Course> findByInstructorEmail(@Param("email") String email);

    List<Course> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    List<Course> findByCategoryIgnoreCase(String category);

    @Query("SELECT c FROM Course c WHERE c.instructor IS NULL")
    List<Course> findCoursesWithoutInstructor();

    @Query("SELECT c FROM Course c")
    List<Course> findAllCoursesWithDetails();
}
