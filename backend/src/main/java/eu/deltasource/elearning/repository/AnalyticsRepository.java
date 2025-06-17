package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Analytics;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, UUID> {

    List<Analytics> findByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);

    List<Analytics> findByUserAndEventTypeAndTimestampBetween(
            User user, Analytics.EventType eventType, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT a.user) FROM Analytics a WHERE a.timestamp >= :startDate")
    long countActiveUsers(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(a) FROM Analytics a WHERE a.eventType = :eventType AND a.timestamp >= :startDate")
    long countEventsByType(@Param("eventType") Analytics.EventType eventType, @Param("startDate") LocalDateTime startDate);

    List<Analytics> findByCourseAndTimestampBetween(Course course, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT a.user) FROM Analytics a WHERE a.course = :course AND a.eventType = 'COURSE_ENROLLMENT'")
    long countEnrollmentsByCourse(@Param("course") Course course);

    @Query("SELECT COUNT(DISTINCT a.user) FROM Analytics a WHERE a.course = :course AND a.eventType = 'COURSE_COMPLETION'")
    long countCompletionsByCourse(@Param("course") Course course);

    @Query("SELECT AVG(a.duration) FROM Analytics a WHERE a.course = :course AND a.eventType IN ('VIDEO_START', 'VIDEO_COMPLETE')")
    Double getAverageWatchTimeByCourse(@Param("course") Course course);

    @Query("SELECT COUNT(a) FROM Analytics a WHERE a.video.id = :videoId AND a.eventType = 'VIDEO_START'")
    long countVideoViews(@Param("videoId") UUID videoId);

    @Query("SELECT AVG(a.duration) FROM Analytics a WHERE a.video.id = :videoId AND a.eventType = 'VIDEO_COMPLETE'")
    Double getAverageVideoWatchTime(@Param("videoId") UUID videoId);

    @Query("SELECT DATE(a.timestamp) as date, COUNT(DISTINCT a.user) as count FROM Analytics a " +
            "WHERE a.timestamp >= :startDate GROUP BY DATE(a.timestamp) ORDER BY date")
    List<Object[]> getDailyActiveUsers(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT MONTH(a.timestamp) as month, YEAR(a.timestamp) as year, COUNT(a) as count FROM Analytics a " +
            "WHERE a.eventType = 'COURSE_ENROLLMENT' AND a.timestamp >= :startDate " +
            "GROUP BY YEAR(a.timestamp), MONTH(a.timestamp) ORDER BY year, month")
    List<Object[]> getMonthlyEnrollments(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT c.name, COUNT(DISTINCT a.user) as enrollments FROM Analytics a " +
            "JOIN a.course c WHERE a.eventType = 'COURSE_ENROLLMENT' " +
            "GROUP BY c.id, c.name ORDER BY enrollments DESC")
    List<Object[]> getPopularCourses(Pageable pageable);

    @Query("SELECT HOUR(a.timestamp) as hour, COUNT(a) as count FROM Analytics a " +
            "WHERE a.user = :user AND a.eventType IN ('VIDEO_START', 'LESSON_START') " +
            "GROUP BY HOUR(a.timestamp) ORDER BY count DESC")
    List<Object[]> getUserLearningPatterns(@Param("user") User user);

    @Query("SELECT a FROM Analytics a WHERE a.user = :user AND a.eventType = 'USER_LOGIN' " +
            "ORDER BY a.timestamp DESC")
    Page<Analytics> getUserLoginHistory(@Param("user") User user, Pageable pageable);

    @Query("SELECT SUM(a.duration) FROM Analytics a WHERE a.user = :user AND a.eventType IN ('VIDEO_COMPLETE', 'LESSON_COMPLETE')")
    Long getTotalLearningTime(@Param("user") User user);

    @Query("SELECT COUNT(DISTINCT a.course) FROM Analytics a WHERE a.user = :user AND a.eventType = 'COURSE_ENROLLMENT'")
    long getCoursesEnrolledCount(@Param("user") User user);

    @Query("SELECT COUNT(DISTINCT a.course) FROM Analytics a WHERE a.user = :user AND a.eventType = 'COURSE_COMPLETION'")
    long getCoursesCompletedCount(@Param("user") User user);

    void deleteByTimestampBefore(LocalDateTime cutoffDate);
}
