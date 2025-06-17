package eu.deltasource.elearning.service;

import eu.deltasource.elearning.model.Analytics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service to automatically track analytics events based on system events
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsEventService {

    private final AnalyticsService analyticsService;

    /**
     * Track user registration
     */
    public void trackUserRegistration(UUID userId) {
        analyticsService.trackEvent(
                eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                        .userId(userId)
                        .eventType(Analytics.EventType.USER_REGISTRATION)
                        .build(),
                null
        );
    }

    /**
     * Track course view
     */
    public void trackCourseView(UUID userId, UUID courseId) {
        analyticsService.trackEvent(
                eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                        .userId(userId)
                        .courseId(courseId)
                        .eventType(Analytics.EventType.COURSE_VIEW)
                        .build(),
                null
        );
    }

    /**
     * Track course completion
     */
    public void trackCourseCompletion(UUID userId, UUID courseId) {
        analyticsService.trackEvent(
                eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                        .userId(userId)
                        .courseId(courseId)
                        .eventType(Analytics.EventType.COURSE_COMPLETION)
                        .build(),
                null
        );
    }

    /**
     * Track video start
     */
    public void trackVideoStart(UUID userId, UUID courseId, UUID videoId) {
        analyticsService.trackVideoEvent(userId, courseId, videoId,
                Analytics.EventType.VIDEO_START, null, null);
    }

    /**
     * Track video completion
     */
    public void trackVideoComplete(UUID userId, UUID courseId, UUID videoId, Long duration) {
        analyticsService.trackVideoEvent(userId, courseId, videoId,
                Analytics.EventType.VIDEO_COMPLETE, duration, 100.0);
    }

    /**
     * Track search query
     */
    public void trackSearchQuery(UUID userId, String query) {
        analyticsService.trackEvent(
                eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                        .userId(userId)
                        .eventType(Analytics.EventType.SEARCH_QUERY)
                        .metadata("{\"query\":\"" + query + "\"}")
                        .build(),
                null
        );
    }
}
