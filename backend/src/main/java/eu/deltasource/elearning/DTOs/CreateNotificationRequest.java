package eu.deltasource.elearning.DTOs;

import eu.deltasource.elearning.model.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new notification")
public class CreateNotificationRequest {

    @Schema(description = "Single user ID (for individual notification)")
    private UUID userId;

    @Schema(description = "Multiple user IDs (for bulk notification)")
    private List<UUID> userIds;

    @NotBlank(message = "Title is required")
    @Schema(description = "Notification title", example = "New Course Available")
    private String title;

    @NotBlank(message = "Message is required")
    @Schema(description = "Notification message", example = "A new course has been added")
    private String message;

    @NotNull(message = "Type is required")
    @Schema(description = "Notification type", example = "COURSE_ENROLLMENT")
    private Notification.NotificationType type;

    @Schema(description = "Notification priority", example = "NORMAL")
    private Notification.NotificationPriority priority = Notification.NotificationPriority.NORMAL;

    @Schema(description = "Related entity ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID relatedEntityId;

    @Schema(description = "Related entity type", example = "COURSE")
    private Notification.RelatedEntityType relatedEntityType;

    @Schema(description = "Action URL for clickable notifications", example = "/courses/123")
    private String actionUrl;
}
