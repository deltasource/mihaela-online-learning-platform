package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.AnalyticsEventDTO;
import eu.deltasource.elearning.DTOs.CourseAnalyticsDTO;
import eu.deltasource.elearning.DTOs.DashboardStatsDTO;
import eu.deltasource.elearning.DTOs.UserAnalyticsDTO;
import eu.deltasource.elearning.model.Analytics;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.User;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final VideoRepository videoRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public void trackEvent(AnalyticsEventDTO eventDTO, HttpServletRequest request) {
        User user = userRepository.findById(eventDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Analytics.AnalyticsBuilder builder = Analytics.builder()
                .user(user)
                .eventType(eventDTO.getEventType())
                .timestamp(eventDTO.getTimestamp() != null ? eventDTO.getTimestamp() : LocalDateTime.now())
                .duration(eventDTO.getDuration())
                .metadata(eventDTO.getMetadata())
                .sessionId(String.valueOf(eventDTO.getSessionId()))
                .completionPercentage(eventDTO.getCompletionPercentage())
                .score(eventDTO.getScore());

        if (eventDTO.getCourseId() != null) {
            Course course = courseRepository.findById(eventDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            builder.course(course);
        }

        if (eventDTO.getVideoId() != null) {
            Video video = videoRepository.findById(eventDTO.getVideoId())
                    .orElseThrow(() -> new RuntimeException("Video not found"));
            builder.video(video);
        }

        if (request != null) {
            builder.ipAddress(getClientIpAddress(request))
                    .userAgent(request.getHeader("User-Agent"));
        }

        Analytics analytics = builder.build();
        analyticsRepository.save(analytics);

        log.debug("Tracked analytics event: {} for user: {}", eventDTO.getEventType(), user.getEmail());
    }

    public DashboardStatsDTO getDashboardStats() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        long totalUsers = userRepository.count();
        long totalCourses = courseRepository.count();
        long totalVideos = videoRepository.count();
        long totalEnrollments = enrollmentRepository.count();
        long activeUsers = analyticsRepository.countActiveUsers(thirtyDaysAgo);
        long newUsersThisMonth = analyticsRepository.countEventsByType(Analytics.EventType.USER_REGISTRATION, startOfMonth);

        long totalCompletions = analyticsRepository.countEventsByType(Analytics.EventType.COURSE_COMPLETION, LocalDateTime.now().minusYears(1));
        double completionRate = totalEnrollments > 0 ? (double) totalCompletions / totalEnrollments * 100 : 0;

        List<Object[]> popularCoursesData = analyticsRepository.getPopularCourses(PageRequest.of(0, 5));
        List<DashboardStatsDTO.PopularCourseDTO> popularCourses = popularCoursesData.stream()
                .map(row -> DashboardStatsDTO.PopularCourseDTO.builder()
                        .courseName((String) row[0])
                        .enrollmentCount((Long) row[1])
                        .averageRating(0.0)
                        .completionCount(0L)
                        .build())
                .collect(Collectors.toList());

        List<Object[]> dailyUsersData = analyticsRepository.getDailyActiveUsers(thirtyDaysAgo);
        Map<String, Long> dailyActiveUsers = dailyUsersData.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));

        List<Object[]> monthlyEnrollmentsData = analyticsRepository.getMonthlyEnrollments(LocalDateTime.now().minusYears(1));
        Map<String, Long> monthlyEnrollments = monthlyEnrollmentsData.stream()
                .collect(Collectors.toMap(
                        row -> row[1] + "-" + String.format("%02d", row[0]), // year-month
                        row -> (Long) row[2]
                ));

        return DashboardStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalCourses(totalCourses)
                .totalVideos(totalVideos)
                .totalEnrollments(totalEnrollments)
                .activeUsers(activeUsers)
                .newUsersThisMonth(newUsersThisMonth)
                .courseCompletionRate(completionRate)
                .averageVideoWatchTime(0.0)
                .popularCourses(popularCourses)
                .dailyActiveUsers(dailyActiveUsers)
                .monthlyEnrollments(monthlyEnrollments)
                .lastUpdated(LocalDateTime.now())
                .build();
    }


    public UserAnalyticsDTO getUserAnalytics(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long totalLearningTime = analyticsRepository.getTotalLearningTime(user);
        long coursesEnrolled = analyticsRepository.getCoursesEnrolledCount(user);
        long coursesCompleted = analyticsRepository.getCoursesCompletedCount(user);

        List<Object[]> learningPatterns = analyticsRepository.getUserLearningPatterns(user);
        int favoriteHour = learningPatterns.isEmpty() ? 9 :
                ((Number) learningPatterns.get(0)[0]).intValue();

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Analytics> recentActivity = analyticsRepository.findByUserAndTimestampBetween(user, thirtyDaysAgo, LocalDateTime.now());

        Map<String, Integer> dailyActivity = recentActivity.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));

        return UserAnalyticsDTO.builder()
                .userId(userId.toString())
                .userFullName(user.getFirstName() + " " + user.getLastName())
                .totalLearningTime(totalLearningTime != null ? totalLearningTime / 60 : 0)
                .coursesEnrolled((int) coursesEnrolled)
                .coursesCompleted((int) coursesCompleted)
                .videosWatched(0)
                .averageSessionDuration(0.0)
                .lastLogin(user.getLastLoginAt())
                .joinDate(user.getCreatedAt())
                .learningStreak(0)
                .favoriteHour(favoriteHour)
                .courseProgress(new ArrayList<>())
                .dailyActivity(dailyActivity)
                .achievements(new ArrayList<>())
                .build();
    }

    public CourseAnalyticsDTO getCourseAnalytics(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        long totalEnrollments = analyticsRepository.countEnrollmentsByCourse(course);
        long totalCompletions = analyticsRepository.countCompletionsByCourse(course);
        double completionRate = totalEnrollments > 0 ? (double) totalCompletions / totalEnrollments * 100 : 0;
        double dropOffRate = 100 - completionRate;

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long activeStudents = analyticsRepository.findByCourseAndTimestampBetween(course, thirtyDaysAgo, LocalDateTime.now())
                .stream()
                .map(Analytics::getUser)
                .collect(Collectors.toSet())
                .size();

        return CourseAnalyticsDTO.builder()
                .courseId(UUID.fromString(courseId.toString()))
                .courseName(course.getName())
                .totalEnrollments(totalEnrollments)
                .activeStudents(activeStudents)
                .completionRate(completionRate)
                .averageCompletionTime(0.0)
                .dropOffRate(dropOffRate)
                .averageRating(0.0)
                .totalWatchTime(0.0)
                .popularVideos(new ArrayList<>())
                .enrollmentTrends(new HashMap<>())
                .videoEngagement(new HashMap<>())
                .dropOffPoints(new ArrayList<>())
                .demographics(CourseAnalyticsDTO.DemographicsDTO.builder()
                        .ageGroups(new HashMap<>())
                        .locations(new HashMap<>())
                        .deviceTypes(new HashMap<>())
                        .build())
                .build();
    }

    public void trackUserLogin(UUID userId, HttpServletRequest request) {
        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .eventType(Analytics.EventType.USER_LOGIN)
                .build();
        trackEvent(eventDTO, request);
    }

    public void trackCourseEnrollment(UUID userId, UUID courseId) {
        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .courseId(courseId)
                .eventType(Analytics.EventType.COURSE_ENROLLMENT)
                .build();
        trackEvent(eventDTO, null);
    }

    public void trackVideoEvent(UUID userId, UUID courseId, UUID videoId,
                                Analytics.EventType eventType, Long duration, Double completionPercentage) {
        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .courseId(courseId)
                .videoId(videoId)
                .eventType(eventType)
                .duration(duration)
                .completionPercentage(completionPercentage)
                .build();
        trackEvent(eventDTO, null);
    }


    @Transactional
    public void cleanupOldData(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        analyticsRepository.deleteByTimestampBefore(cutoffDate);
        log.info("Cleaned up analytics data older than {} days", daysToKeep);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
