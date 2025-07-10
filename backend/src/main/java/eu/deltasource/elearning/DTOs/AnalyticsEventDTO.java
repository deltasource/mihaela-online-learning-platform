package eu.deltasource.elearning.DTOs;

import eu.deltasource.elearning.model.Analytics;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Analytics event data")
public class AnalyticsEventDTO {

    @Schema(description = "Event ID")
    private UUID id;

    @NotNull(message = "User ID is required")
    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(description = "Course ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID courseId;

    @Schema(description = "Video ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID videoId;

    @NotNull(message = "Event type is required")
    @Schema(description = "Type of event", example = "VIDEO_START")
    private Analytics.EventType eventType;

    @Schema(description = "Event timestamp")
    private LocalDateTime timestamp;

    @Schema(description = "Duration in seconds", example = "300")
    private Long duration;

    @Schema(description = "Additional metadata as JSON string")
    private String metadata;

    @Schema(description = "IP address", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "User agent string")
    private String userAgent;

    @Schema(description = "Session identifier")
    private UUID sessionId;

    @Schema(description = "Completion percentage", example = "75.5")
    private Double completionPercentage;

    @Schema(description = "Score or rating", example = "85.0")
    private Double score;

    @Schema(description = "User full name")
    private String userFullName;

    @Schema(description = "Course name")
    private String courseName;

    @Schema(description = "Video title")
    private String videoTitle;
}
