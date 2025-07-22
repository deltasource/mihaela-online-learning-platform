package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.CreateNotificationRequest;
import eu.deltasource.elearning.DTOs.NotificationDTO;
import eu.deltasource.elearning.DTOs.NotificationSummaryDTO;
import eu.deltasource.elearning.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Notification Management", description = "Operations for managing user notifications")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Create a notification", description = "Creates a new notification for one or multiple users")
    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<?> createNotification(@Valid @RequestBody CreateNotificationRequest request) {
        log.info("Creating notification with request: {}", request);
        if (request.getUserId() != null) {
            log.info("Creating notification with user with ID: {}", request.getUserId());
            NotificationDTO notification = notificationService.createNotification(request);
            log.info("Notification created successfully: {}", notification);
            return ResponseEntity.status(CREATED).body(notification);
        } else if (request.getUserIds() != null && !request.getUserIds().isEmpty()) {
            log.info("Creating bulk notifications for user IDs: {}", request.getUserIds());
            List<NotificationDTO> notifications = notificationService.createBulkNotifications(request);
            log.info("Bulk notifications created successfully: {}", notifications);
            return ResponseEntity.status(CREATED).body(notifications);
        } else {
            log.warn("Invalid request: either userId or userIds must be provided");
            return ResponseEntity.badRequest().body("Either userId or userIds must be provided");
        }
    }

    @Operation(summary = "Get user notifications", description = "Retrieves paginated notifications for a user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<NotificationDTO>> getUserNotifications(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        log.info("Retrieving notifications for user with ID: {} (page: {}, size: {})", userId, page, size);
        Page<NotificationDTO> notifications = notificationService.getUserNotifications(userId, page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Get unread notifications", description = "Retrieves all unread notifications for a user")
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(
            @Parameter(description = "User ID") @PathVariable UUID userId) {

        log.info("Retrieving unread notifications for user with ID: {}", userId);
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Get notification summary", description = "Retrieves notification summary for a user")
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<NotificationSummaryDTO> getNotificationSummary(
            @Parameter(description = "User ID") @PathVariable UUID userId) {

        log.info("Retrieving notification summary for user with ID: {}", userId);
        NotificationSummaryDTO summary = notificationService.getNotificationSummary(userId);
        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "Mark notification as read", description = "Marks a specific notification as read")
    @PutMapping("/{notificationId}/read")
    @ResponseStatus(NO_CONTENT)
    public void markAsRead(
            @Parameter(description = "Notification ID") @PathVariable UUID notificationId) {

        log.info("Marking notification with ID: {} as read", notificationId);
        notificationService.markAsRead(notificationId);
    }

    @Operation(summary = "Mark all notifications as read", description = "Marks all notifications as read for a user")
    @PutMapping("/user/{userId}/read-all")
    @ResponseStatus(NO_CONTENT)
    public void markAllAsRead(
            @Parameter(description = "User ID") @PathVariable UUID userId) {

        log.info("Marking all notifications as read for user with ID: {}", userId);
        notificationService.markAllAsRead(userId);
    }

    @Operation(summary = "Delete notification", description = "Deletes a specific notification")
    @DeleteMapping("/{notificationId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteNotification(
            @Parameter(description = "Notification ID") @PathVariable UUID notificationId) {

        log.info("Deleting notification with ID: {}", notificationId);
        notificationService.deleteNotification(notificationId);
    }

    @Operation(summary = "Clean up old notifications", description = "Deletes old notifications for a user")
    @DeleteMapping("/user/{userId}/cleanup")
    @ResponseStatus(NO_CONTENT)
    public void cleanupOldNotifications(
            @Parameter(description = "User ID") @PathVariable UUID userId,
            @Parameter(description = "Days old") @RequestParam(defaultValue = "30") int daysOld) {

        log.info("Cleaning up notifications older than {} days for user with ID: {}", daysOld, userId);
        notificationService.cleanupOldNotifications(userId, daysOld);
    }
}
