package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.AnalyticsEventDTO;
import eu.deltasource.elearning.DTOs.CourseAnalyticsDTO;
import eu.deltasource.elearning.DTOs.DashboardStatsDTO;
import eu.deltasource.elearning.DTOs.UserAnalyticsDTO;
import eu.deltasource.elearning.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Analytics", description = "Analytics and reporting operations")
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(summary = "Track analytics event", description = "Records an analytics event for tracking user behavior")
    @PostMapping("/events")
    @ResponseStatus(CREATED)
    public void trackEvent(@Valid @RequestBody AnalyticsEventDTO eventDTO, HttpServletRequest request) {
        log.info("Tracking event: {}", eventDTO);
        analyticsService.trackEvent(eventDTO, request);
        log.info("Tracked event: {}", eventDTO);
    }

    @Operation(summary = "Get dashboard statistics", description = "Retrieves overall platform statistics for admin dashboard")
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        log.info("Getting dashboard statistics");
        DashboardStatsDTO stats = analyticsService.getDashboardStats();
        log.info("Retrieved dashboard statistics: {}", stats);
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Get user analytics", description = "Retrieves analytics data for a specific user")
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #userId == authentication.principal.id) or (hasRole('INSTRUCTOR') and #userId == authentication.principal.id)")
    public ResponseEntity<UserAnalyticsDTO> getUserAnalytics(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        log.info("Getting analytics for user: {}", userId);
        UserAnalyticsDTO analytics = analyticsService.getUserAnalytics(userId);
        log.info("Retrieved analytics for user {}: {}", userId, analytics);
        return ResponseEntity.ok(analytics);
    }

    @Operation(summary = "Get course analytics", description = "Retrieves analytics data for a specific course")
    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseAnalyticsDTO> getCourseAnalytics(
            @Parameter(description = "Course ID") @PathVariable UUID courseId) {
        log.info("Getting analytics for course: {}", courseId);
        CourseAnalyticsDTO analytics = analyticsService.getCourseAnalytics(courseId);
        log.info("Retrieved analytics for course {}: {}", courseId, analytics);
        return ResponseEntity.ok(analytics);
    }

    @Operation(summary = "Track user login", description = "Records a user login event")
    @PostMapping("/login/{userId}")
    public void trackUserLogin(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            HttpServletRequest request) {
        log.info("Tracking login for user: {}", userId);
        analyticsService.trackUserLogin(userId, request);
        log.info("Tracked login for user: {}", userId);
    }

    @Operation(summary = "Track course enrollment", description = "Records a course enrollment event")
    @PostMapping("/enrollment")
    public void trackCourseEnrollment(
            @Parameter(description = "User ID") @RequestParam UUID userId,
            @Parameter(description = "Course ID") @RequestParam UUID courseId) {
        log.info("Tracking course enrollment for user: {} in course: {}", userId, courseId);
        analyticsService.trackCourseEnrollment(userId, courseId);
        log.info("Tracked course enrollment for user: {} in course: {}", userId, courseId);
    }

    @Operation(summary = "Clean up old analytics data", description = "Removes analytics data older than specified days")
    @DeleteMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void cleanupOldData(
            @Parameter(description = "Days to keep") @RequestParam(defaultValue = "365") int daysToKeep) {
        log.info("Cleaning up analytics data older than {} days", daysToKeep);
        analyticsService.cleanupOldData(daysToKeep);
        log.info("Cleaned up analytics data older than {} days", daysToKeep);
    }
}
