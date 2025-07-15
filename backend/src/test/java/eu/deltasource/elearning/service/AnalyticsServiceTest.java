package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.AnalyticsEventDTO;
import eu.deltasource.elearning.model.Analytics;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.User;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.repository.AnalyticsRepository;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.UserRepository;
import eu.deltasource.elearning.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsServiceTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void givenValidEventDTO_whenTrackEvent_thenVerifyRepositorySaveCalled() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //When
        AnalyticsEventDTO eventDTO = AnalyticsEventDTO.builder()
                .userId(userId)
                .eventType(Analytics.EventType.USER_LOGIN)
                .build();

        // Then
        analyticsService.trackEvent(eventDTO, null);
        verify(analyticsRepository, times(1)).save(any(Analytics.class));
    }

    @Test
    void givenValidUserId_whenTrackUserLogin_thenVerifyRepositorySaveCalled() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        analyticsService.trackUserLogin(userId, null);

        // Then
        verify(analyticsRepository, times(1)).save(any(Analytics.class));
    }

    @Test
    void givenValidUserIdAndCourseId_whenTrackCourseEnrollment_thenVerifyRepositorySaveCalled() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Course course = new Course();
        course.setId(courseId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // When
        analyticsService.trackCourseEnrollment(userId, courseId);

        // Then
        verify(analyticsRepository, times(1)).save(any(Analytics.class));
    }

    @Test
    void givenValidUserIdCourseIdAndVideoId_whenTrackVideoEvent_thenVerifyRepositorySaveCalled() {
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

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));

        // Then
        analyticsService.trackVideoEvent(userId, courseId, videoId, Analytics.EventType.VIDEO_START, 120L, 50.0);
        verify(analyticsRepository, times(1)).save(any(Analytics.class));
    }


    @Test
    void givenDaysToKeep_whenCleanupOldData_thenVerifyRepositoryDeleteCalled() {
        // Given
        int daysToKeep = 30;

        // When
        analyticsService.cleanupOldData(daysToKeep);

        // Then
        verify(analyticsRepository, times(1)).deleteByTimestampBefore(any());
    }
}
