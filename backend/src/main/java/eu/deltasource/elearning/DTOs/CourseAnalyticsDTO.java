package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Course-specific analytics data")
public class CourseAnalyticsDTO {

    @Schema(description = "Course ID")
    private String courseId;

    @Schema(description = "Course name")
    private String courseName;

    @Schema(description = "Total enrollments")
    private long totalEnrollments;

    @Schema(description = "Active students (accessed in last 30 days)")
    private long activeStudents;

    @Schema(description = "Completion rate as percentage")
    private double completionRate;

    @Schema(description = "Average completion time in days")
    private double averageCompletionTime;

    @Schema(description = "Drop-off rate as percentage")
    private double dropOffRate;

    @Schema(description = "Average rating")
    private double averageRating;

    @Schema(description = "Total video watch time in hours")
    private double totalWatchTime;

    @Schema(description = "Most watched videos")
    private List<VideoStatsDTO> popularVideos;

    @Schema(description = "Enrollment trends by month")
    private Map<String, Long> enrollmentTrends;

    @Schema(description = "Student engagement by video")
    private Map<String, Double> videoEngagement;

    @Schema(description = "Common drop-off points (video positions)")
    private List<DropOffPointDTO> dropOffPoints;

    @Schema(description = "Student demographics")
    private DemographicsDTO demographics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoStatsDTO {
        private String videoId;
        private String videoTitle;
        private long viewCount;
        private double averageWatchTime;
        private double completionRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DropOffPointDTO {
        private String videoId;
        private String videoTitle;
        private double timePosition;
        private long dropOffCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DemographicsDTO {
        private Map<String, Long> ageGroups;
        private Map<String, Long> locations;
        private Map<String, Long> deviceTypes;
    }
}
