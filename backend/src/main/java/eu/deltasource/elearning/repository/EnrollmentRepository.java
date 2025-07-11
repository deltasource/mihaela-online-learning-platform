package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.enums.EnrollmentStatus;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Enrollment;
import eu.deltasource.elearning.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByStudent(Student student);

    List<Enrollment> findByCourse(Course course);

    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);

    List<Enrollment> findByStatus(EnrollmentStatus status);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course = :course AND e.status = :status")
    long countByCourseAndStatus(@Param("course") Course course, @Param("status") EnrollmentStatus status);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student = :student AND e.status = :status")
    long countByStudentAndStatus(@Param("student") Student student, @Param("status") EnrollmentStatus status);

    boolean existsByStudentAndCourse(Student student, Course course);
}
