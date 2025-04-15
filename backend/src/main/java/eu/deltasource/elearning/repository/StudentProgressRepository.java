package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.model.StudentProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {

    Optional<StudentProgress> findByStudentAndCourse(Student student, Course course);

    Optional<StudentProgress> findByStudentIdAndCourseId(UUID studentId, UUID courseId);

    List<StudentProgress> findByStudentId(UUID studentId);
}
