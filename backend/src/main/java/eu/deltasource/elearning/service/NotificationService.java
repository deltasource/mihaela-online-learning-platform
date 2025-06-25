package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.CreateNotificationRequest;
import eu.deltasource.elearning.DTOs.NotificationDTO;
import eu.deltasource.elearning.DTOs.NotificationSummaryDTO;
import eu.deltasource.elearning.model.Notification;
import eu.deltasource.elearning.model.User;
import eu.deltasource.elearning.repository.NotificationRepository;
import eu.deltasource.elearning.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public NotificationDTO createNotification(CreateNotificationRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required for single notification");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        Notification notification = Notification.builder()
                .user(user)
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .priority(request.getPriority())
                .relatedEntityId(request.getRelatedEntityId())
                .relatedEntityType(request.getRelatedEntityType())
                .actionUrl(request.getActionUrl())
                .build();

        notification = notificationRepository.save(notification);
        log.info("Created notification {} for user {}", notification.getId(), user.getEmail());

        return mapToDTO(notification);
    }

    @Transactional
    public List<NotificationDTO> createBulkNotifications(CreateNotificationRequest request) {
        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            throw new IllegalArgumentException("User IDs are required for bulk notification");
        }

        List<User> users = userRepository.findAllById(request.getUserIds());
        if (users.size() != request.getUserIds().size()) {
            throw new RuntimeException("Some users were not found");
        }

        List<Notification> notifications = users.stream()
                .map(user -> Notification.builder()
                        .user(user)
                        .title(request.getTitle())
                        .message(request.getMessage())
                        .type(request.getType())
                        .priority(request.getPriority())
                        .relatedEntityId(request.getRelatedEntityId())
                        .relatedEntityType(request.getRelatedEntityType())
                        .actionUrl(request.getActionUrl())
                        .build())
                .collect(Collectors.toList());

        notifications = notificationRepository.saveAll(notifications);
        log.info("Created {} bulk notifications", notifications.size());

        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Page<NotificationDTO> getUserNotifications(UUID userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        return notifications.map(this::mapToDTO);
    }

    public List<NotificationDTO> getUnreadNotifications(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        List<Notification> notifications = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);

        return notifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(UUID notificationId) {
        notificationRepository.markAsRead(notificationId, LocalDateTime.now());
        log.info("Marked notification {} as read", notificationId);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        notificationRepository.markAllAsReadForUser(user, LocalDateTime.now());
        log.info("Marked all notifications as read for user {}", user.getEmail());
    }

    public NotificationSummaryDTO getNotificationSummary(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        long totalNotifications = notificationRepository.countByUser(user);
        long unreadNotifications = notificationRepository.countByUserAndIsReadFalse(user);
        long highPriorityNotifications = notificationRepository.countByUserAndPriorityAndIsReadFalse(
                user, Notification.NotificationPriority.HIGH);
        long urgentNotifications = notificationRepository.countByUserAndPriorityAndIsReadFalse(
                user, Notification.NotificationPriority.URGENT);

        return NotificationSummaryDTO.builder()
                .totalNotifications(totalNotifications)
                .unreadNotifications(unreadNotifications)
                .highPriorityNotifications(highPriorityNotifications)
                .urgentNotifications(urgentNotifications)
                .build();
    }

    @Transactional
    public void deleteNotification(UUID notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new RuntimeException("Notification not found with ID: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
        log.info("Deleted notification {}", notificationId);
    }

    @Transactional
    public void cleanupOldNotifications(UUID userId, int daysOld) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        notificationRepository.deleteByUserAndCreatedAtBefore(user, cutoffDate);
        log.info("Cleaned up old notifications for user {} older than {} days", user.getEmail(), daysOld);
    }

    public void createSystemNotification(UUID userId, String title, String message,
                                         Notification.NotificationType type) {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(type)
                .priority(Notification.NotificationPriority.NORMAL)
                .build();

        createNotification(request);
    }

    public void createCourseNotification(UUID userId, UUID courseId, String title, String message,
                                         Notification.NotificationType type) {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(type)
                .priority(Notification.NotificationPriority.NORMAL)
                .relatedEntityId(courseId)
                .relatedEntityType(Notification.RelatedEntityType.COURSE)
                .actionUrl("/courses/" + courseId)
                .build();

        createNotification(request);
    }

    private NotificationDTO mapToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .priority(notification.getPriority())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .relatedEntityId(notification.getRelatedEntityId())
                .relatedEntityType(notification.getRelatedEntityType())
                .actionUrl(notification.getActionUrl())
                .userFullName(notification.getUser().getFirstName() + " " + notification.getUser().getLastName())
                .build();
    }
}
