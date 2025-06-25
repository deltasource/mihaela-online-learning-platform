package eu.deltasource.elearning.DTOs;

import eu.deltasource.elearning.model.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Notification information")
public class NotificationDTO {

    @Schema(description = "Notification ID")
    private UUID id;

    @NotNull(message = "User ID is required")
    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @NotBlank(message = "Title is required")
    @Schema(description = "Notification title", example = "New Course Available")
    private String title;

    @NotBlank(message = "Message is required")
    @Schema(description = "Notification message", example = "A new course 'Advanced Java' has been added to your learning path")
    private String message;

    @NotNull(message = "Type is required")
    @Schema(description = "Notification type", example = "COURSE_ENROLLMENT")
    private Notification.NotificationType type;

    @Schema(description = "Notification priority", example = "NORMAL")
    private Notification.NotificationPriority priority = Notification.NotificationPriority.NORMAL;

    @Schema(description = "Whether the notification has been read", example = "false")
    private boolean isRead;

    @Schema(description = "When the notification was created")
    private LocalDateTime createdAt;

    @Schema(description = "When the notification was read")
    private LocalDateTime readAt;

    @Schema(description = "Related entity ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID relatedEntityId;

    @Schema(description = "Related entity type", example = "COURSE")
    private Notification.RelatedEntityType relatedEntityType;

    @Schema(description = "Action URL for clickable notifications", example = "/courses/123")
    private String actionUrl;

    @Schema(description = "User's full name")
    private String userFullName;

    @Schema(description = "Related entity name (e.g., course name)")
    private String relatedEntityName;
}
