package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.AnalyticsEventDTO;
import eu.deltasource.elearning.DTOs.CourseAnalyticsDTO;
import eu.deltasource.elearning.DTOs.DashboardStatsDTO;
import eu.deltasource.elearning.DTOs.UserAnalyticsDTO;
import eu.deltasource.elearning.model.Analytics;
import eu.deltasource.elearning.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalyticsService analyticsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidEventDTO_whenTrackEvent_thenReturnsCreatedAndServiceCalled() throws Exception {
        // Given
        AnalyticsEventDTO eventDTO = new AnalyticsEventDTO();
        eventDTO.setEventType(Analytics.EventType.USER_LOGIN);
        eventDTO.setUserId(UUID.randomUUID());
        eventDTO.setTimestamp(LocalDateTime.now());

        // When & Then
        mockMvc.perform(post("/api/analytics/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isCreated());

        verify(analyticsService, times(1)).trackEvent(any(AnalyticsEventDTO.class), any(HttpServletRequest.class));
    }

    @Test
    void givenDashboardRequest_whenGetDashboardStats_thenReturnsStatsDTO() throws Exception {
        // Given
        DashboardStatsDTO statsDTO = new DashboardStatsDTO();
        when(analyticsService.getDashboardStats()).thenReturn(statsDTO);

        // When & Then
        mockMvc.perform(get("/api/analytics/dashboard"))
                .andExpect(status().isOk());
        verify(analyticsService, times(1)).getDashboardStats();
    }

    @Test
    void givenUserId_whenGetUserAnalytics_thenReturnsUserAnalyticsDTO() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UserAnalyticsDTO userAnalyticsDTO = new UserAnalyticsDTO();
        when(analyticsService.getUserAnalytics(userId)).thenReturn(userAnalyticsDTO);

        // When & Then
        mockMvc.perform(get("/api/analytics/users/{userId}", userId))
                .andExpect(status().isOk());
        verify(analyticsService, times(1)).getUserAnalytics(eq(userId));
    }

    @Test
    void givenCourseId_whenGetCourseAnalytics_thenReturnsCourseAnalyticsDTO() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        CourseAnalyticsDTO courseAnalyticsDTO = new CourseAnalyticsDTO();
        when(analyticsService.getCourseAnalytics(courseId)).thenReturn(courseAnalyticsDTO);

        // When & Then
        mockMvc.perform(get("/api/analytics/courses/{courseId}", courseId))
                .andExpect(status().isOk());
        verify(analyticsService, times(1)).getCourseAnalytics(eq(courseId));
    }

    @Test
    void givenUserId_whenTrackUserLogin_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(post("/api/analytics/login/{userId}", userId))
                .andExpect(status().isOk());
        verify(analyticsService, times(1)).trackUserLogin(eq(userId), any(HttpServletRequest.class));
    }

    @Test
    void givenUserIdAndCourseId_whenTrackCourseEnrollment_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();

        // When & Then
        mockMvc.perform(post("/api/analytics/enrollment")
                        .param("userId", userId.toString())
                        .param("courseId", courseId.toString()))
                .andExpect(status().isOk());
        verify(analyticsService, times(1)).trackCourseEnrollment(eq(userId), eq(courseId));
    }

    @Test
    void givenDaysToKeep_whenCleanupOldData_thenReturnsNoContentAndServiceCalled() throws Exception {
        // Given
        int daysToKeep = 365;

        // When & Then
        mockMvc.perform(delete("/api/analytics/cleanup")
                        .param("daysToKeep", String.valueOf(daysToKeep)))
                .andExpect(status().isNoContent());
        verify(analyticsService, times(1)).cleanupOldData(eq(daysToKeep));
    }
}
