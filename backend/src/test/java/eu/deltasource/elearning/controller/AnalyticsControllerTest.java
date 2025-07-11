package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.AnalyticsEventDTO;
import eu.deltasource.elearning.DTOs.CourseAnalyticsDTO;
import eu.deltasource.elearning.DTOs.DashboardStatsDTO;
import eu.deltasource.elearning.DTOs.UserAnalyticsDTO;
import eu.deltasource.elearning.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AnalyticsControllerTest {

    @Mock
    private AnalyticsService analyticsService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AnalyticsController analyticsController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void givenValidEventDTO_whenTrackEvent_thenVerifyServiceCalled() {
        // Given
        AnalyticsEventDTO eventDTO = new AnalyticsEventDTO();

        // When
        analyticsController.trackEvent(eventDTO, request);

        // Then
        verify(analyticsService, times(1)).trackEvent(eventDTO, request);
    }

    @Test
    void givenRequestForDashboardStats_whenGetDashboardStats_thenReturnStatsDTO() {
        // Given
        DashboardStatsDTO statsDTO = new DashboardStatsDTO();
        when(analyticsService.getDashboardStats()).thenReturn(statsDTO);

        // When
        DashboardStatsDTO response = analyticsController.getDashboardStats().getBody();

        // Then
        assertEquals(statsDTO, response);
        verify(analyticsService, times(1)).getDashboardStats();
    }

    @Test
    void givenUserId_whenGetUserAnalytics_thenReturnUserAnalyticsDTO() {
        // Given
        UUID userId = UUID.randomUUID();
        UserAnalyticsDTO userAnalyticsDTO = new UserAnalyticsDTO();
        when(analyticsService.getUserAnalytics(userId)).thenReturn(userAnalyticsDTO);

        // When
        UserAnalyticsDTO response = analyticsController.getUserAnalytics(userId).getBody();

        // Then
        assertEquals(userAnalyticsDTO, response);
        verify(analyticsService, times(1)).getUserAnalytics(userId);
    }

    @Test
    void givenCourseId_whenGetCourseAnalytics_thenReturnCourseAnalyticsDTO() {
        // Given
        UUID courseId = UUID.randomUUID();
        CourseAnalyticsDTO courseAnalyticsDTO = new CourseAnalyticsDTO();
        when(analyticsService.getCourseAnalytics(courseId)).thenReturn(courseAnalyticsDTO);

        // When
        CourseAnalyticsDTO response = analyticsController.getCourseAnalytics(courseId).getBody();

        // Then
        assertEquals(courseAnalyticsDTO, response);
        verify(analyticsService, times(1)).getCourseAnalytics(courseId);
    }

    @Test
    void givenUserId_whenTrackUserLogin_thenVerifyServiceCalled() {
        // Given
        UUID userId = UUID.randomUUID();

        // When
        analyticsController.trackUserLogin(userId, request);

        // Then
        verify(analyticsService, times(1)).trackUserLogin(userId, request);
    }

    @Test
    void givenUserIdAndCourseId_whenTrackCourseEnrollment_thenVerifyServiceCalled() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        // When
        analyticsController.trackCourseEnrollment(userId, courseId);

        // Then
        verify(analyticsService, times(1)).trackCourseEnrollment(userId, courseId);
    }

    @Test
    void givenDaysToKeep_whenCleanupOldData_thenVerifyServiceCalled() {
        // Given
        int daysToKeep = 365;

        // When
        analyticsController.cleanupOldData(daysToKeep);

        // Then
        verify(analyticsService, times(1)).cleanupOldData(daysToKeep);
    }
}
