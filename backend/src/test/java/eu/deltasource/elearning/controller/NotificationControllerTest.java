package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.CreateNotificationRequest;
import eu.deltasource.elearning.DTOs.NotificationDTO;
import eu.deltasource.elearning.DTOs.NotificationSummaryDTO;
import eu.deltasource.elearning.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void givenValidRequest_whenCreateNotification_thenReturnNotificationDTO() {
        // Given
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setUserId(UUID.randomUUID());
        request.setMessage("Test notification");
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(UUID.randomUUID());
        when(notificationService.createNotification(request)).thenReturn(notificationDTO);

        // When
        var response = notificationController.createNotification(request).getBody();

        // Then
        assertEquals(notificationDTO, response);
        verify(notificationService, times(1)).createNotification(request);
    }

    @Test
    void givenValidUserId_whenGetUnreadNotifications_thenReturnListOfNotificationDTOs() {
        // Given
        UUID userId = UUID.randomUUID();
        List<NotificationDTO> notifications = List.of(new NotificationDTO(), new NotificationDTO());
        when(notificationService.getUnreadNotifications(userId)).thenReturn(notifications);

        // When
        List<NotificationDTO> response = notificationController.getUnreadNotifications(userId).getBody();

        // Then
        assertEquals(notifications, response);
        verify(notificationService, times(1)).getUnreadNotifications(userId);
    }

    @Test
    void givenValidUserId_whenGetNotificationSummary_thenReturnNotificationSummaryDTO() {
        // Given
        UUID userId = UUID.randomUUID();
        NotificationSummaryDTO summaryDTO = new NotificationSummaryDTO();
        when(notificationService.getNotificationSummary(userId)).thenReturn(summaryDTO);

        // When
        NotificationSummaryDTO response = notificationController.getNotificationSummary(userId).getBody();

        // Then
        assertEquals(summaryDTO, response);
        verify(notificationService, times(1)).getNotificationSummary(userId);
    }

    @Test
    void givenValidNotificationId_whenMarkAsRead_thenVerifyServiceCalled() {
        // Given
        UUID notificationId = UUID.randomUUID();
        doNothing().when(notificationService).markAsRead(notificationId);

        // When
        notificationController.markAsRead(notificationId);

        // Then
        verify(notificationService, times(1)).markAsRead(notificationId);
    }

    @Test
    void givenValidUserId_whenMarkAllAsRead_thenVerifyServiceCalled() {
        // Given
        UUID userId = UUID.randomUUID();
        doNothing().when(notificationService).markAllAsRead(userId);

        // When
        notificationController.markAllAsRead(userId);

        // Then
        verify(notificationService, times(1)).markAllAsRead(userId);
    }

    @Test
    void givenValidNotificationId_whenDeleteNotification_thenVerifyServiceCalled() {
        // Given
        UUID notificationId = UUID.randomUUID();
        doNothing().when(notificationService).deleteNotification(notificationId);

        // When
        notificationController.deleteNotification(notificationId);

        // Then
        verify(notificationService, times(1)).deleteNotification(notificationId);
    }

    @Test
    void givenValidUserIdAndDaysOld_whenCleanupOldNotifications_thenVerifyServiceCalled() {
        // Given
        UUID userId = UUID.randomUUID();
        int daysOld = 30;
        doNothing().when(notificationService).cleanupOldNotifications(userId, daysOld);

        // When
        notificationController.cleanupOldNotifications(userId, daysOld);

        // Then
        verify(notificationService, times(1)).cleanupOldNotifications(userId, daysOld);
    }
}
