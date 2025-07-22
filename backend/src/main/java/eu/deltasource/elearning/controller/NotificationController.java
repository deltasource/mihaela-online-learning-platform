package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.CreateNotificationRequest;
import eu.deltasource.elearning.DTOs.NotificationDTO;
import eu.deltasource.elearning.DTOs.NotificationSummaryDTO;
import eu.deltasource.elearning.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public NotificationDTO createNotification(@RequestBody @Valid CreateNotificationRequest request) {
        log.info("Creating notification: {}", request);
        return notificationService.createNotification(request);
    }

    @PostMapping("/bulk")
    public List<NotificationDTO> createBulkNotifications(@RequestBody @Valid CreateNotificationRequest request) {
        log.info("Creating bulk notifications: {}", request);
        return notificationService.createBulkNotifications(request);
    }

    @GetMapping("/user/{userId}")
    public Page<NotificationDTO> getUserNotifications(
            @PathVariable UUID userId,
            @RequestParam int page,
            @RequestParam int size) {
        log.info("Retrieving notifications for user ID: {} with page: {} and size: {}", userId, page, size);
        return notificationService.getUserNotifications(userId, page, size);
    }

    @GetMapping("/user/{userId}/unread")
    public List<NotificationDTO> getUnreadNotifications(@PathVariable UUID userId) {
        log.info("Retrieving unread notifications for user ID: {}", userId);
        return notificationService.getUnreadNotifications(userId);
    }

    @PostMapping("/{notificationId}/read")
    public void markAsRead(@PathVariable UUID notificationId) {
        log.info("Marking notification ID: {} as read", notificationId);
        notificationService.markAsRead(notificationId);
    }

    @PostMapping("/user/{userId}/read-all")
    public void markAllAsRead(@PathVariable UUID userId) {
        log.info("Marking all notifications as read for user ID: {}", userId);
        notificationService.markAllAsRead(userId);
    }

    @GetMapping("/user/{userId}/summary")
    public NotificationSummaryDTO getNotificationSummary(@PathVariable UUID userId) {
        log.info("Retrieving notification summary for user ID: {}", userId);
        return notificationService.getNotificationSummary(userId);
    }

    @DeleteMapping("/{notificationId}")
    public void deleteNotification(@PathVariable UUID notificationId) {
        log.info("Deleting notification ID: {}", notificationId);
        notificationService.deleteNotification(notificationId);
    }

    @DeleteMapping("/user/{userId}/cleanup")
    public void cleanupOldNotifications(@PathVariable UUID userId, @RequestParam int daysOld) {
        log.info("Cleaning up notifications older than {} days for user ID: {}", daysOld, userId);
        notificationService.cleanupOldNotifications(userId, daysOld);
    }
}
