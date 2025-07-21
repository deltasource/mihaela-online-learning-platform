package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.StudentProgressDTO;
import eu.deltasource.elearning.service.StudentProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Student Progress ", description = "Track how much percent of the course the student has finished")
@RestController
@RequiredArgsConstructor
@RequestMapping("/students/progress/v1")
@Slf4j
public class StudentProgressController {

    private final StudentProgressService studentProgressService;

    @GetMapping("/{studentId}/courses/{courseId}")
    @Operation(summary = "Get student progress in a course", description = "Returns progress percentage for a student in a specific course")
    public StudentProgressDTO getProgressPercentage(
            @Parameter(description = "UUID of the student")
            @PathVariable @NotNull UUID studentId,
            @Parameter(description = "UUID of the course")
            @PathVariable @NotNull UUID courseId) {
        log.info("Retrieving progress for student ID: {} in course ID: {}", studentId, courseId);
        return studentProgressService.getProgressPercentage(studentId, courseId);
    }

    @PostMapping("/{studentId}/courses/{courseId}/videos/{videoId}/update")
    @Operation(summary = "Update progress as student watches a video", description = "Marks a video as watched and updates progress")
    public void updateProgress(
            @Parameter(description = "UUID of the student")
            @PathVariable @NotNull UUID studentId,
            @Parameter(description = "UUID of the course")
            @PathVariable @NotNull UUID courseId,
            @Parameter(description = "UUID of the video")
            @PathVariable @NotNull UUID videoId) {
        log.info("Updating progress for student ID: {} in course ID: {} for video ID: {}", studentId, courseId, videoId);
        studentProgressService.updateProgress(studentId, courseId, videoId);
    }
}
