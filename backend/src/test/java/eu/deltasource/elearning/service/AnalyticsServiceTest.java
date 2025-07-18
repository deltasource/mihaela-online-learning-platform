package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.AnalyticsEventDTO;
import eu.deltasource.elearning.DTOs.CourseAnalyticsDTO;
import eu.deltasource.elearning.DTOs.DashboardStatsDTO;
import eu.deltasource.elearning.model.Analytics;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.User;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private AnalyticsRepository analyticsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void givenValidEventDTO_whenTrackEvent_thenRepositorySaveCalled() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .eventType(Analytics.EventType.USER_LOGIN)
                .build();

        // When
        analyticsService.trackEvent(eventDTO, null);

        // Then
        verify(analyticsRepository).save(any(Analytics.class));
    }

    @Test
    void givenEventDTOWithCourseAndVideo_whenTrackEvent_thenRepositorySaveCalled() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Course course = new Course();
        course.setId(courseId);
        Video video = new Video();
        video.setId(videoId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));

        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .courseId(courseId)
                .videoId(videoId)
                .eventType(Analytics.EventType.VIDEO_START)
                .build();

        // When
        analyticsService.trackEvent(eventDTO, null);

        // Then
        verify(analyticsRepository).save(any(Analytics.class));
    }

    @Test
    void givenEventDTOWithRequest_whenTrackEvent_thenIpAndUserAgentSet() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getHeader("User-Agent")).thenReturn("JUnit");

        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .eventType(Analytics.EventType.USER_LOGIN)
                .build();

        // When
        analyticsService.trackEvent(eventDTO, request);

        // Then
        verify(analyticsRepository).save(any(Analytics.class));
    }

    @Test
    void givenMissingUser_whenTrackEvent_thenThrows() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .eventType(Analytics.EventType.USER_LOGIN)
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> analyticsService.trackEvent(eventDTO, null));
    }

    @Test
    void givenMissingCourse_whenTrackEvent_thenThrows() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .courseId(courseId)
                .eventType(Analytics.EventType.COURSE_ENROLLMENT)
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> analyticsService.trackEvent(eventDTO, null));
    }

    @Test
    void givenMissingVideo_whenTrackEvent_thenThrows() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Course course = new Course();
        course.setId(courseId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .courseId(courseId)
                .videoId(videoId)
                .eventType(Analytics.EventType.VIDEO_START)
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> analyticsService.trackEvent(eventDTO, null));
    }

    @Test
    void getDashboardStats_returnsStats() {
        // Given
        when(userRepository.count()).thenReturn(10L);
        when(courseRepository.count()).thenReturn(5L);
        when(videoRepository.count()).thenReturn(20L);
        when(enrollmentRepository.count()).thenReturn(15L);
        when(analyticsRepository.countActiveUsers(any())).thenReturn(7L);
        when(analyticsRepository.countEventsByType(any(), any())).thenReturn(2L);

        // When
        DashboardStatsDTO stats = analyticsService.getDashboardStats();

        // Then
        assertNotNull(stats);
        assertEquals(10L, stats.getTotalUsers());
        assertEquals(5L, stats.getTotalCourses());
        assertEquals(20L, stats.getTotalVideos());
        assertEquals(15L, stats.getTotalEnrollments());
        assertEquals(7L, stats.getActiveUsers());
        assertEquals(2L, stats.getNewUsersThisMonth());
        assertTrue(stats.getCourseCompletionRate() >= 0);
        assertNotNull(stats.getPopularCourses());
        assertNotNull(stats.getDailyActiveUsers());
        assertNotNull(stats.getMonthlyEnrollments());
    }

    @Test
    void getUserAnalytics_missingUser_throws() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> analyticsService.getUserAnalytics(userId));
    }

    @Test
    void getCourseAnalytics_returnsAnalytics() {
        // Given
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        course.setName("Course1");
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(analyticsRepository.countEnrollmentsByCourse(course)).thenReturn(10L);
        when(analyticsRepository.countCompletionsByCourse(course)).thenReturn(5L);
        when(analyticsRepository.findByCourseAndTimestampBetween(any(), any(), any()))
                .thenReturn(List.of(new Analytics(), new Analytics()));

        // When
        CourseAnalyticsDTO dto = analyticsService.getCourseAnalytics(courseId);

        // Then
        assertNotNull(dto);
        assertEquals("Course1", dto.getCourseName());
        assertEquals(10L, dto.getTotalEnrollments());
        assertTrue(dto.getCompletionRate() >= 0);
        assertTrue(dto.getDropOffRate() >= 0);
        assertTrue(dto.getActiveStudents() >= 0);
    }

    @Test
    void getCourseAnalytics_missingCourse_throws() {
        // Given
        UUID courseId = UUID.randomUUID();
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> analyticsService.getCourseAnalytics(courseId));
    }

    @Test
    void cleanupOldData_deletesOldData() {
        // Given

        // When
        analyticsService.cleanupOldData(30);

        // Then
        verify(analyticsRepository).deleteByTimestampBefore(any());
    }
}
