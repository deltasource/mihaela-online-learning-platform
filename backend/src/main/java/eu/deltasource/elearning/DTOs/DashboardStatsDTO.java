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
@Schema(description = "Dashboard statistics")
public class DashboardStatsDTO {

    @Schema(description = "Total number of users")
    private long totalUsers;

    @Schema(description = "Total number of courses")
    private long totalCourses;

    @Schema(description = "Total number of videos")
    private long totalVideos;

    @Schema(description = "Total number of enrollments")
    private long totalEnrollments;

    @Schema(description = "Active users in the last 30 days")
    private long activeUsers;

    @Schema(description = "New users this month")
    private long newUsersThisMonth;

    @Schema(description = "Course completion rate as percentage")
    private double courseCompletionRate;

    @Schema(description = "Average video watch time in minutes")
    private double averageVideoWatchTime;

    @Schema(description = "Most popular courses")
    private List<PopularCourseDTO> popularCourses;

    @Schema(description = "User activity by day")
    private Map<String, Long> dailyActiveUsers;

    @Schema(description = "Enrollment trends by month")
    private Map<String, Long> monthlyEnrollments;

    @Schema(description = "Last updated timestamp")
    private LocalDateTime lastUpdated;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopularCourseDTO {
        private String courseName;
        private long enrollmentCount;
        private double averageRating;
        private long completionCount;
    }
}
