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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        CreateNotificationRequest request = new CreateNotificationRequest(); // missing required fields

        // When
        var result = mockMvc.perform(post("/api/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidRequest_whenCreateBulkNotifications_thenReturnsBadRequest() throws Exception {
        // Given
        CreateNotificationRequest request = new CreateNotificationRequest();

        // When
        var result = mockMvc.perform(post("/api/notifications/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenValidUserIdAndPaging_whenGetUserNotifications_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        Page<NotificationDTO> page = new PageImpl<>(List.of(new NotificationDTO()));
        when(notificationService.getUserNotifications(eq(userId), eq(0), eq(10))).thenReturn(page);

        // When
        var result = mockMvc.perform(get("/api/notifications/user/{userId}", userId)
                .param("page", "0")
                .param("size", "10"));

        // Then
        result.andExpect(status().isOk());
        verify(notificationService, times(1)).getUserNotifications(eq(userId), eq(0), eq(10));
    }

    @Test
    void givenInvalidUUID_whenGetUserNotifications_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(get("/api/notifications/user/{userId}", "invalid-uuid")
                .param("page", "0")
                .param("size", "10"));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenValidUserId_whenGetUnreadNotifications_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        List<NotificationDTO> response = List.of(new NotificationDTO());
        when(notificationService.getUnreadNotifications(userId)).thenReturn(response);

        // When
        var result = mockMvc.perform(get("/api/notifications/user/{userId}/unread", userId));

        // Then
        result.andExpect(status().isOk());
        verify(notificationService, times(1)).getUnreadNotifications(eq(userId));
    }

    @Test
    void givenInvalidUUID_whenGetUnreadNotifications_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(get("/api/notifications/user/{userId}/unread", "invalid-uuid"));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenValidNotificationId_whenMarkAsRead_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID notificationId = UUID.randomUUID();
        doNothing().when(notificationService).markAsRead(notificationId);

        // When
        var result = mockMvc.perform(post("/api/notifications/{notificationId}/read", notificationId));

        // Then
        result.andExpect(status().isOk());
        verify(notificationService, times(1)).markAsRead(eq(notificationId));
    }

    @Test
    void givenInvalidUUID_whenMarkAsRead_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(post("/api/notifications/{notificationId}/read", "invalid-uuid"));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenValidUserId_whenMarkAllAsRead_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        doNothing().when(notificationService).markAllAsRead(userId);

        // When
        var result = mockMvc.perform(post("/api/notifications/user/{userId}/read-all", userId));

        // Then
        result.andExpect(status().isOk());
        verify(notificationService, times(1)).markAllAsRead(eq(userId));
    }

    @Test
    void givenInvalidUUID_whenMarkAllAsRead_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(post("/api/notifications/user/{userId}/read-all", "invalid-uuid"));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenValidUserId_whenGetNotificationSummary_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        NotificationSummaryDTO summary = new NotificationSummaryDTO();
        when(notificationService.getNotificationSummary(userId)).thenReturn(summary);

        // When
        var result = mockMvc.perform(get("/api/notifications/user/{userId}/summary", userId));

        // Then
        result.andExpect(status().isOk());
        verify(notificationService, times(1)).getNotificationSummary(eq(userId));
    }

    @Test
    void givenInvalidUUID_whenGetNotificationSummary_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(get("/api/notifications/user/{userId}/summary", "invalid-uuid"));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenValidNotificationId_whenDeleteNotification_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID notificationId = UUID.randomUUID();
        doNothing().when(notificationService).deleteNotification(notificationId);

        // When
        var result = mockMvc.perform(delete("/api/notifications/{notificationId}", notificationId));

        // Then
        result.andExpect(status().isOk());
        verify(notificationService, times(1)).deleteNotification(eq(notificationId));
    }

    @Test
    void givenInvalidUUID_whenDeleteNotification_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(delete("/api/notifications/{notificationId}", "invalid-uuid"));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenValidUserIdAndDaysOld_whenCleanupOldNotifications_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        int daysOld = 30;
        doNothing().when(notificationService).cleanupOldNotifications(userId, daysOld);

        // When
        var result = mockMvc.perform(delete("/api/notifications/user/{userId}/cleanup", userId)
                .param("daysOld", String.valueOf(daysOld)));

        // Then
        result.andExpect(status().isOk());
        verify(notificationService, times(1)).cleanupOldNotifications(eq(userId), eq(daysOld));
    }

    @Test
    void givenInvalidUUID_whenCleanupOldNotifications_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(delete("/api/notifications/user/{userId}/cleanup", "invalid-uuid")
                .param("daysOld", "30"));

        // Then
        result.andExpect(status().isBadRequest());
    }
}
