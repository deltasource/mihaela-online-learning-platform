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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Analytics", description = "Analytics and reporting operations")
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(summary = "Track analytics event", description = "Records an analytics event for tracking user behavior")
    @PostMapping("/events")
    @ResponseStatus(CREATED)
    public void trackEvent(@Valid @RequestBody AnalyticsEventDTO eventDTO, HttpServletRequest request) {
        analyticsService.trackEvent(eventDTO, request);
    }

    @Operation(summary = "Get dashboard statistics", description = "Retrieves overall platform statistics for admin dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = analyticsService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Get user analytics", description = "Retrieves analytics data for a specific user")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserAnalyticsDTO> getUserAnalytics(
            @Parameter(description = "User ID") @PathVariable UUID userId) {
        UserAnalyticsDTO analytics = analyticsService.getUserAnalytics(userId);
        return ResponseEntity.ok(analytics);
    }

    @Operation(summary = "Get course analytics", description = "Retrieves analytics data for a specific course")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseAnalyticsDTO> getCourseAnalytics(
            @Parameter(description = "Course ID") @PathVariable UUID courseId) {
        CourseAnalyticsDTO analytics = analyticsService.getCourseAnalytics(courseId);
        return ResponseEntity.ok(analytics);
    }

    @Operation(summary = "Track user login", description = "Records a user login event")
    @PostMapping("/login/{userId}")
    public void trackUserLogin(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            HttpServletRequest request) {
        analyticsService.trackUserLogin(userId, request);
    }

    @Operation(summary = "Track course enrollment", description = "Records a course enrollment event")
    @PostMapping("/enrollment")
    public void trackCourseEnrollment(
            @Parameter(description = "User ID") @RequestParam UUID userId,
            @Parameter(description = "Course ID") @RequestParam UUID courseId) {
        analyticsService.trackCourseEnrollment(userId, courseId);
    }

    @Operation(summary = "Clean up old analytics data", description = "Removes analytics data older than specified days")
    @DeleteMapping("/cleanup")
    @ResponseStatus(NO_CONTENT)
    public void cleanupOldData(
            @Parameter(description = "Days to keep") @RequestParam(defaultValue = "365") int daysToKeep) {
        analyticsService.cleanupOldData(daysToKeep);
    }
}
