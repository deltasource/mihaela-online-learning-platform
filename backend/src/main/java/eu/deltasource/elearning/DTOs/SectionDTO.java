package eu.deltasource.elearning.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Course section with lessons")
public class SectionDTO {

    @Schema(description = "Section ID")
    private String id;

    @Schema(description = "Section title")
    private String title;

    @Schema(description = "Course ID this section belongs to")
    private UUID courseId;

    @Schema(description = "Section order in the course")
    private Integer order;

    @Schema(description = "Lessons in this section")
    private List<LessonDTO> lessons = new ArrayList<>();
}
