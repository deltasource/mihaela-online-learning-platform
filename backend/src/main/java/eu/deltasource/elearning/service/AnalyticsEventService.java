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
        try {
            analyticsService.trackEvent(
                    eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                            .userId(userId)
                            .eventType(Analytics.EventType.USER_REGISTRATION)
                            .build(),
                    null
            );
            log.info("Tracked user registration for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error tracking user registration for user ID: {}", userId, e);
            throw e;
        }
    }

    public void trackCourseView(UUID userId, UUID courseId) {
        log.info("Tracking course view: userId={}, courseId={}", userId, courseId); // log method entry and params
        try {
            analyticsService.trackEvent(
                    eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                            .userId(userId)
                            .courseId(courseId)
                            .eventType(Analytics.EventType.COURSE_VIEW)
                            .build(),
                    null
            );
            log.debug("Successfully tracked course view event for userId={} and courseId={}", userId, courseId); // log success
        } catch (Exception e) {
            log.error("Failed to track course view event for userId={} and courseId={}", userId, courseId, e); // log errors
            throw e;
        }
    }

    public void trackCourseCompletion(UUID userId, UUID courseId) {
        log.info("Tracking course completion: userId={}, courseId={}", userId, courseId);
        try {
            analyticsService.trackEvent(
                    eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                            .userId(userId)
                            .courseId(courseId)
                            .eventType(Analytics.EventType.COURSE_COMPLETION)
                            .build(),
                    null
            );
            log.info("Tracked course completion event for userId={} and courseId={}", userId, courseId);
        } catch (Exception e) {
            log.error("Error tracking course completion for userId={} and courseId={}", userId, courseId, e);
            throw e;
        }
    }

    public void trackVideoStart(UUID userId, UUID courseId, UUID videoId) {
        log.info("Tracking video start: userId={}, courseId={}, videoId={}", userId, courseId, videoId);
        try {
            analyticsService.trackVideoEvent(
                    userId,
                    courseId,
                    videoId,
                    Analytics.EventType.VIDEO_START,
                    null,
                    null
            );
            log.info("Tracked video start event for userId={}, courseId={}, videoId={}", userId, courseId, videoId);
        } catch (Exception e) {
            log.error("Error tracking video start for userId={}, courseId={}, videoId={}", userId, courseId, videoId, e);
            throw e;
        }
    }

    public void trackVideoComplete(UUID userId, UUID courseId, UUID videoId, Long duration) {
        log.info("Tracking video completion: userId={}, courseId={}, videoId={}, duration={}", userId, courseId, videoId, duration);
        try {
            analyticsService.trackVideoEvent(
                    userId,
                    courseId,
                    videoId,
                    Analytics.EventType.VIDEO_COMPLETE,
                    duration,
                    100.0
            );
            log.info("Tracked video completion event for userId={}, courseId={}, videoId={}, duration={}", userId, courseId, videoId, duration);
        } catch (Exception e) {
            log.error("Error tracking video completion for userId={}, courseId={}, videoId={}, duration={}", userId, courseId, videoId, duration, e);
            throw e;
        }
    }

    public void trackSearchQuery(UUID userId, String query) {
        log.info("Tracking search query: userId={}, query={}", userId, query);
        try {
            analyticsService.trackEvent(
                    eu.deltasource.elearning.DTOs.AnalyticsEventDTO.builder()
                            .userId(userId)
                            .eventType(Analytics.EventType.SEARCH_QUERY)
                            .metadata("{\"query\":\"" + query + "\"}")
                            .build(),
                    null
            );
            log.info("Tracked search query event for userId={}, query={}", userId, query);
        } catch (Exception e) {
            log.error("Error tracking search query for userId={}, query={}", userId, query, e);
            throw e;
        }
    }
}
