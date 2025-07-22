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

    public void trackUserRegistration(UUID userId) {
        log.info("Tracking user registration for user ID: {}", userId);
        analyticsService.trackEvent(
                eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                        .userId(userId)
                        .eventType(Analytics.EventType.USER_REGISTRATION)
                        .build(),
                null
        );
        log.info("User registration tracked for user ID: {}", userId);
    }

    public void trackCourseView(UUID userId, UUID courseId) {
        log.info("Tracking course view for user ID: {}, course ID: {}", userId, courseId);
        analyticsService.trackEvent(
                eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                        .userId(userId)
                        .courseId(courseId)
                        .eventType(Analytics.EventType.COURSE_VIEW)
                        .build(),
                null
        );
        log.info("Course view tracked for user ID: {}, course ID: {}", userId, courseId);
    }


    public void trackCourseCompletion(UUID userId, UUID courseId) {
        log.info("Tracking course completion for user ID: {}, course ID: {}", userId, courseId);
        analyticsService.trackEvent(
                eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                        .userId(userId)
                        .courseId(courseId)
                        .eventType(Analytics.EventType.COURSE_COMPLETION)
                        .build(),
                null
        );
        log.info("Course completion tracked for user ID: {}, course ID: {}", userId, courseId);
    }

    public void trackVideoStart(UUID userId, UUID courseId, UUID videoId) {
        log.info("Tracking video start for user ID: {}, course ID: {}, video ID: {}", userId, courseId, videoId);
        analyticsService.trackVideoEvent(userId, courseId, videoId,
                Analytics.EventType.VIDEO_START, null, null);
        log.info("Video start tracked for user ID: {}, course ID: {}, video ID: {}", userId, courseId, videoId);
    }

    public void trackVideoComplete(UUID userId, UUID courseId, UUID videoId, Long duration) {
        log.info("Tracking video completion for user ID: {}, course ID: {}, video ID: {}, duration: {}", userId, courseId, videoId, duration);
        analyticsService.trackVideoEvent(userId, courseId, videoId,
                Analytics.EventType.VIDEO_COMPLETE, duration, 100.0);
        log.info("Video completion tracked for user ID: {}, course ID: {}, video ID: {}, duration: {}", userId, courseId, videoId, duration);
    }

    public void trackSearchQuery(UUID userId, String query) {
        log.info("Tracking search query for user ID: {}, query: {}", userId, query);
        analyticsService.trackEvent(
                eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                        .userId(userId)
                        .eventType(Analytics.EventType.SEARCH_QUERY)
                        .metadata("{\"query\":\"" + query + "\"}")
                        .build(),
                null
        );
        log.info("Search query tracked for user ID: {}, query: {}", userId, query);
    }
}
