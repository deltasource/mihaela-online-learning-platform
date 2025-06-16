package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Notification summary for a user")
public class NotificationSummaryDTO {

    @Schema(description = "Total number of notifications", example = "25")
    private long totalNotifications;

    @Schema(description = "Number of unread notifications", example = "5")
    private long unreadNotifications;

    @Schema(description = "Number of high priority notifications", example = "2")
    private long highPriorityNotifications;

    @Schema(description = "Number of urgent notifications", example = "1")
    private long urgentNotifications;
}
