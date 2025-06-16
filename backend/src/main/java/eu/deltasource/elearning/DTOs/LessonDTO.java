package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Lesson in a course section")
public class LessonDTO {

    @Schema(description = "Lesson ID")
    private String id;

    @Schema(description = "Lesson title")
    private String title;

    @Schema(description = "Lesson description")
    private String description;

    @Schema(description = "Video URL for this lesson")
    private String videoUrl;

    @Schema(description = "Duration in seconds")
    private Integer duration;

    @Schema(description = "Section ID this lesson belongs to")
    private String sectionId;

    @Schema(description = "Lesson order in the section")
    private Integer order;

    @Schema(description = "Whether this lesson is a preview")
    private Boolean isPreview;
}
