package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.CreateNotificationRequest;
import eu.deltasource.elearning.DTOs.NotificationDTO;
import eu.deltasource.elearning.DTOs.NotificationSummaryDTO;
import eu.deltasource.elearning.model.Notification;
import eu.deltasource.elearning.model.User;
import eu.deltasource.elearning.repository.NotificationRepository;
import eu.deltasource.elearning.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void givenValidRequest_whenCreateNotification_thenSavesAndReturnsDTO() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .userId(userId)
                .title("Title")
                .message("Message")
                .type(Notification.NotificationType.COURSE_COMPLETION)
                .priority(Notification.NotificationPriority.NORMAL)
                .build();
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .user(user)
                .title("Title")
                .message("Message")
                .type(Notification.NotificationType.COURSE_ENROLLMENT)
                .priority(Notification.NotificationPriority.NORMAL)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // When
        NotificationDTO dto = notificationService.createNotification(request);

        // Then
        assertEquals("Title", dto.getTitle());
        assertEquals(userId, dto.getUserId());
        assertEquals("Test User", dto.getUserFullName());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void givenUserId_whenGetUserNotifications_thenReturnsPage() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .user(user)
                .title("Title")
                .message("Message")
                .type(Notification.NotificationType.COURSE_UPDATE)
                .priority(Notification.NotificationPriority.NORMAL)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        Page<Notification> page = new PageImpl<>(List.of(notification));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.findByUserOrderByCreatedAtDesc(eq(user), any(Pageable.class))).thenReturn(page);

        // When
        Page<NotificationDTO> result = notificationService.getUserNotifications(userId, 0, 10);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals("Title", result.getContent().get(0).getTitle());
    }

    @Test
    void givenUserId_whenGetUnreadNotifications_thenReturnsList() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .user(user)
                .title("Unread")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user))
                .thenReturn(List.of(notification));

        // When
        List<NotificationDTO> result = notificationService.getUnreadNotifications(userId);

        // Then
        assertEquals(1, result.size());
        assertEquals("Unread", result.get(0).getTitle());
    }

    @Test
    void givenNotificationId_whenMarkAsRead_thenRepositoryCalled() {
        // Given
        UUID notificationId = UUID.randomUUID();

        // When
        notificationService.markAsRead(notificationId);

        // Then
        verify(notificationRepository).markAsRead(eq(notificationId), any(LocalDateTime.class));
    }

    @Test
    void givenUserId_whenMarkAllAsRead_thenRepositoryCalled() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        notificationService.markAllAsRead(userId);

        // Then
        verify(notificationRepository).markAllAsReadForUser(eq(user), any(LocalDateTime.class));
    }

    @Test
    void givenUserId_whenGetNotificationSummary_thenReturnsSummary() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.countByUser(user)).thenReturn(10L);
        when(notificationRepository.countByUserAndIsReadFalse(user)).thenReturn(2L);
        when(notificationRepository.countByUserAndPriorityAndIsReadFalse(user, Notification.NotificationPriority.HIGH)).thenReturn(1L);
        when(notificationRepository.countByUserAndPriorityAndIsReadFalse(user, Notification.NotificationPriority.URGENT)).thenReturn(0L);

        // When
        NotificationSummaryDTO summary = notificationService.getNotificationSummary(userId);

        // Then
        assertEquals(10L, summary.getTotalNotifications());
        assertEquals(2L, summary.getUnreadNotifications());
        assertEquals(1L, summary.getHighPriorityNotifications());
        assertEquals(0L, summary.getUrgentNotifications());
    }

    @Test
    void givenNotificationId_whenDeleteNotification_thenDeletesIfExists() {
        // Given
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.existsById(notificationId)).thenReturn(true);

        // When
        notificationService.deleteNotification(notificationId);

        // Then
        verify(notificationRepository).deleteById(notificationId);
    }

    @Test
    void givenNotificationId_whenDeleteNotification_thenThrowsIfNotExists() {
        // Given
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.existsById(notificationId)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> notificationService.deleteNotification(notificationId));
    }
}
