package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User-specific analytics data")
public class UserAnalyticsDTO {

    @Schema(description = "User ID")
    private String userId;

    @Schema(description = "User full name")
    private String userFullName;

    @Schema(description = "Total learning time in minutes")
    private long totalLearningTime;

    @Schema(description = "Number of courses enrolled")
    private int coursesEnrolled;

    @Schema(description = "Number of courses completed")
    private int coursesCompleted;

    @Schema(description = "Number of videos watched")
    private int videosWatched;

    @Schema(description = "Average session duration in minutes")
    private double averageSessionDuration;

    @Schema(description = "Last login time")
    private LocalDateTime lastLogin;

    @Schema(description = "Account creation date")
    private LocalDateTime joinDate;

    @Schema(description = "Learning streak in days")
    private int learningStreak;

    @Schema(description = "Favorite learning time (hour of day)")
    private int favoriteHour;

    @Schema(description = "Course progress details")
    private List<CourseProgressDTO> courseProgress;

    @Schema(description = "Daily activity (last 30 days)")
    private Map<String, Integer> dailyActivity;

    @Schema(description = "Learning goals and achievements")
    private List<AchievementDTO> achievements;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseProgressDTO {
        private String courseId;
        private String courseName;
        private double progressPercentage;
        private int videosWatched;
        private int totalVideos;
        private LocalDateTime lastAccessed;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AchievementDTO {
        private String title;
        private String description;
        private LocalDateTime achievedAt;
        private String badgeIcon;
    }
}
