package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Student progress in a specific course")
public class StudentProgressDTO {

    @Schema(description = "Student's unique ID", example = "c7b76e5d-30f6-4962-a9e6-395b7a7898d6", requiredMode = REQUIRED)
    private String studentId;

    @Schema(description = "Course's unique ID", example = "df7c8c71-30f6-4962-b3c2-375a13ab8a97",requiredMode = REQUIRED)
    private String courseId;

    @Schema(description = "Percentage of the course completed by the student", example = "45.5", requiredMode = REQUIRED)
    private double progressPercentage;

    @Schema(description = "Total number of videos in the course", example = "10", requiredMode = REQUIRED)
    private int totalVideos;

    @Schema(description = "Number of videos watched by the student", example = "4", requiredMode = REQUIRED)
    private int videosWatched;
}
