package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.CreateNotificationRequest;
import eu.deltasource.elearning.DTOs.NotificationDTO;
import eu.deltasource.elearning.DTOs.NotificationSummaryDTO;
import eu.deltasource.elearning.model.Notification;
import eu.deltasource.elearning.model.User;
import eu.deltasource.elearning.repository.NotificationRepository;
import eu.deltasource.elearning.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder().id(userId).firstName("John").lastName("Doe").email("john@doe.com").build();
    }

    @Test
    void givenValidRequest_whenCreateNotification_thenSavesAndReturnsDTO() {
        // Given
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .userId(userId)
                .title("Title")
                .message("Message")
                .type(Notification.NotificationType.COURSE_UPDATE)
                .priority(Notification.NotificationPriority.NORMAL)
                .build();
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
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // When
        NotificationDTO result = notificationService.createNotification(request);

        // Then
        assertEquals("Title", result.getTitle());
        assertEquals("Message", result.getMessage());
        assertEquals(userId, result.getUserId());
    }

    @Test
    void givenNullUserId_whenCreateNotification_thenThrowsException() {
        // Given
        CreateNotificationRequest request = CreateNotificationRequest.builder().build();

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> notificationService.createNotification(request));
    }

    @Test
    void givenNonExistentUser_whenCreateNotification_thenThrowsException() {
        // Given
        CreateNotificationRequest request = CreateNotificationRequest.builder().userId(userId).build();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(RuntimeException.class, () -> notificationService.createNotification(request));
    }

    @Test
    void givenValidRequest_whenCreateBulkNotifications_thenSavesAndReturnsDTOs() {
        // Given
        List<UUID> userIds = List.of(userId);
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .userIds(userIds)
                .title("Bulk")
                .message("BulkMsg")
                .type(Notification.NotificationType.COURSE_UPDATE)
                .priority(Notification.NotificationPriority.NORMAL)
                .build();
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .user(user)
                .title("Bulk")
                .message("BulkMsg")
                .type(Notification.NotificationType.COURSE_UPDATE)
                .priority(Notification.NotificationPriority.NORMAL)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();
        when(userRepository.findAllById(userIds)).thenReturn(List.of(user));
        when(notificationRepository.saveAll(anyList())).thenReturn(List.of(notification));

        // When
        List<NotificationDTO> result = notificationService.createBulkNotifications(request);

        // Then
        assertEquals(1, result.size());
        assertEquals("Bulk", result.get(0).getTitle());
    }

    @Test
    void givenEmptyUserIds_whenCreateBulkNotifications_thenThrowsException() {
        // Given
        CreateNotificationRequest request = CreateNotificationRequest.builder().userIds(Collections.emptyList()).build();

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> notificationService.createBulkNotifications(request));
    }

    @Test
    void givenSomeUsersNotFound_whenCreateBulkNotifications_thenThrowsException() {
        // Given
        List<UUID> userIds = List.of(userId, UUID.randomUUID());
        CreateNotificationRequest request = CreateNotificationRequest.builder().userIds(userIds).build();
        when(userRepository.findAllById(userIds)).thenReturn(List.of(user));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> notificationService.createBulkNotifications(request));
    }

    @Test
    void givenUserId_whenGetUserNotifications_thenReturnsPage() {
        // Given
        Notification notification = Notification.builder().id(UUID.randomUUID()).user(user).title("T").message("M").createdAt(LocalDateTime.now()).isRead(false).build();
        Page<Notification> page = new PageImpl<>(List.of(notification));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.findByUserOrderByCreatedAtDesc(eq(user), any(Pageable.class))).thenReturn(page);

        // When
        Page<NotificationDTO> result = notificationService.getUserNotifications(userId, 0, 10);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals("T", result.getContent().get(0).getTitle());
    }

    @Test
    void givenNonExistentUser_whenGetUserNotifications_thenThrowsException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(RuntimeException.class, () -> notificationService.getUserNotifications(userId, 0, 10));
    }

    @Test
    void givenUserId_whenGetUnreadNotifications_thenReturnsList() {
        // Given
        Notification notification = Notification.builder().id(UUID.randomUUID()).user(user).title("T").message("M").createdAt(LocalDateTime.now()).isRead(false).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user)).thenReturn(List.of(notification));

        // When
        List<NotificationDTO> result = notificationService.getUnreadNotifications(userId);

        // Then
        assertEquals(1, result.size());
        assertFalse(result.get(0).isRead());
    }

    @Test
    void givenNotificationId_whenMarkAsRead_thenRepositoryCalled() {
        // Given
        UUID notificationId = UUID.randomUUID();
        doNothing().when(notificationRepository).markAsRead(eq(notificationId), any(LocalDateTime.class));

        // When
        notificationService.markAsRead(notificationId);

        // Then
        verify(notificationRepository).markAsRead(eq(notificationId), any(LocalDateTime.class));
    }

    @Test
    void givenUserId_whenMarkAllAsRead_thenRepositoryCalled() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(notificationRepository).markAllAsReadForUser(eq(user), any(LocalDateTime.class));

        // When
        notificationService.markAllAsRead(userId);

        // Then
        verify(notificationRepository).markAllAsReadForUser(eq(user), any(LocalDateTime.class));
    }

    @Test
    void givenNonExistentUser_whenMarkAllAsRead_thenThrowsException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(RuntimeException.class, () -> notificationService.markAllAsRead(userId));
    }

    @Test
    void givenUserId_whenGetNotificationSummary_thenReturnsSummary() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.countByUser(user)).thenReturn(10L);
        when(notificationRepository.countByUserAndIsReadFalse(user)).thenReturn(3L);
        when(notificationRepository.countByUserAndPriorityAndIsReadFalse(user, Notification.NotificationPriority.HIGH)).thenReturn(1L);
        when(notificationRepository.countByUserAndPriorityAndIsReadFalse(user, Notification.NotificationPriority.URGENT)).thenReturn(2L);

        // When
        NotificationSummaryDTO summary = notificationService.getNotificationSummary(userId);

        // Then
        assertEquals(10L, summary.getTotalNotifications());
        assertEquals(3L, summary.getUnreadNotifications());
        assertEquals(1L, summary.getHighPriorityNotifications());
        assertEquals(2L, summary.getUrgentNotifications());
    }

    @Test
    void givenNonExistentUser_whenGetNotificationSummary_thenThrowsException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(RuntimeException.class, () -> notificationService.getNotificationSummary(userId));
    }

    @Test
    void givenNotificationId_whenDeleteNotification_thenDeletes() {
        // Given
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.existsById(notificationId)).thenReturn(true);
        doNothing().when(notificationRepository).deleteById(notificationId);

        // When
        notificationService.deleteNotification(notificationId);

        // Then
        verify(notificationRepository).deleteById(notificationId);
    }

    @Test
    void givenNonExistentNotificationId_whenDeleteNotification_thenThrowsException() {
        // Given
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.existsById(notificationId)).thenReturn(false);

        // When
        // Then
        assertThrows(RuntimeException.class, () -> notificationService.deleteNotification(notificationId));
    }

    @Test
    void givenUserIdAndDays_whenCleanupOldNotifications_thenDeletes() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(notificationRepository).deleteByUserAndCreatedAtBefore(eq(user), any(LocalDateTime.class));

        // When
        notificationService.cleanupOldNotifications(userId, 30);

        // Then
        verify(notificationRepository).deleteByUserAndCreatedAtBefore(eq(user), any(LocalDateTime.class));
    }

    @Test
    void givenNonExistentUser_whenCleanupOldNotifications_thenThrowsException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When

        // Then
        assertThrows(RuntimeException.class, () -> notificationService.cleanupOldNotifications(userId, 30));
    }
}
