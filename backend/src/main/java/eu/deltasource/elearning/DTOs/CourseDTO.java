package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Course Data Transfer Object")
public class CourseDTO {

    @Schema(description = "Course ID")
    private UUID id;

    @NotBlank(message = "Course name is required")
    @Schema(description = "Course name", example = "Java Programming")
    private String name;

    @Schema(description = "Course title (alternative field name)")
    private String title;

    @NotBlank(message = "Course description is required")
    @Schema(description = "Course description", example = "Learn Java from scratch")
    private String description;

    @Schema(description = "Instructor ID for the course")
    private UUID instructorId;

    @Schema(description = "Instructor name")
    private String instructorName;

    @Schema(description = "Course thumbnail URL")
    private String thumbnail;

    @Schema(description = "Course category")
    private String category;

    @Schema(description = "Course level")
    private String level;

    @Schema(description = "Course price")
    private Double price;

    @Schema(description = "Course rating")
    private Double rating;

    @Schema(description = "Total students enrolled")
    private Integer totalStudents;

    @Schema(description = "Creation date")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date")
    private LocalDateTime updatedAt;

    @Schema(description = "List of student IDs enrolled in the course")
    private List<UUID> studentIds;

    @Schema(description = "Course sections with lessons")
    private List<SectionDTO> sections = new ArrayList<>();

    public String getName() {
        return name != null ? name : title;
    }

    public String getTitle() {
        return title != null ? title : name;
    }
}
