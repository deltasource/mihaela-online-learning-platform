package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.CreateNotificationRequest;
import eu.deltasource.elearning.DTOs.NotificationDTO;
import eu.deltasource.elearning.DTOs.NotificationSummaryDTO;
import eu.deltasource.elearning.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The @AutoConfigureMockMvc(addFilters = false) annotation is used in Spring Boot testing to disable the automatic addition of Spring Security filters when configuring MockMvc.
 * By default, when @AutoConfigureMockMvc is used, any registered filters (such as security filters) are applied to the MockMvc instance. Setting addFilters = false prevents these filters from being added, allowing tests to bypass security constraints and focus on controller logic without authentication or authorization requirements.
 **/
@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenInvalidRequest_whenCreateNotification_thenReturnsBadRequest() throws Exception {
        // Given
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setMessage("Missing userId and userIds");

        // When & Then
        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidUserId_whenGetUserNotifications_thenReturnsOk() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        var page = new PageImpl<>(List.of(new NotificationDTO(), new NotificationDTO()), PageRequest.of(0, 20), 2);
        when(notificationService.getUserNotifications(eq(userId), anyInt(), anyInt())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/notifications/user/{userId}", userId)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidUserId_whenGetUnreadNotifications_thenReturnsOk() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        List<NotificationDTO> notifications = List.of(new NotificationDTO(), new NotificationDTO());
        when(notificationService.getUnreadNotifications(userId)).thenReturn(notifications);

        // When & Then
        mockMvc.perform(get("/api/notifications/user/{userId}/unread", userId))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidUserId_whenGetNotificationSummary_thenReturnsOk() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        NotificationSummaryDTO summaryDTO = new NotificationSummaryDTO();
        when(notificationService.getNotificationSummary(userId)).thenReturn(summaryDTO);

        // When & Then
        mockMvc.perform(get("/api/notifications/user/{userId}/summary", userId))
                .andExpect(status().isOk());
    }

    @Test
    void givenValidNotificationId_whenMarkAsRead_thenReturnsNoContent() throws Exception {
        // Given
        UUID notificationId = UUID.randomUUID();
        doNothing().when(notificationService).markAsRead(notificationId);

        // When & Then
        mockMvc.perform(put("/api/notifications/{notificationId}/read", notificationId))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenValidUserId_whenMarkAllAsRead_thenReturnsNoContent() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        doNothing().when(notificationService).markAllAsRead(userId);

        // When & Then
        mockMvc.perform(put("/api/notifications/user/{userId}/read-all", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenValidNotificationId_whenDeleteNotification_thenReturnsNoContent() throws Exception {
        // Given
        UUID notificationId = UUID.randomUUID();
        doNothing().when(notificationService).deleteNotification(notificationId);

        // When & Then
        mockMvc.perform(delete("/api/notifications/{notificationId}", notificationId))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenValidUserIdAndDaysOld_whenCleanupOldNotifications_thenReturnsNoContent() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        int daysOld = 30;
        doNothing().when(notificationService).cleanupOldNotifications(userId, daysOld);

        // When & Then
        mockMvc.perform(delete("/api/notifications/user/{userId}/cleanup", userId)
                        .param("daysOld", String.valueOf(daysOld)))
                .andExpect(status().isNoContent());
    }
}
