package eu.deltasource.elearning.service;

import eu.deltasource.elearning.model.Analytics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventServiceTest {

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Test
    void givenUserId_whenTrackUserRegistration_thenDelegatesToAnalyticsService() {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        analyticsEventService.trackUserRegistration(userId);

        // Then
        verify(analyticsService, times(1)).trackEvent(
                argThat(event -> event.getUserId().equals(userId)
                        && event.getEventType() == Analytics.EventType.USER_REGISTRATION),
                isNull()
        );
    }

    @Test
    void givenUserIdAndCourseId_whenTrackCourseCompletion_thenDelegatesToAnalyticsService() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        // When
        analyticsEventService.trackCourseCompletion(userId, courseId);

        // Then
        verify(analyticsService, times(1)).trackEvent(
                argThat(event -> event.getUserId().equals(userId)
                        && event.getCourseId().equals(courseId)
                        && event.getEventType() == Analytics.EventType.COURSE_COMPLETION),
                isNull()
        );
    }
}
