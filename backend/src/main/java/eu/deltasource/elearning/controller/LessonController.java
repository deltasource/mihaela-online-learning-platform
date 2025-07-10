package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.LessonDTO;
import eu.deltasource.elearning.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Lesson Management")
@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a new lesson")
    public LessonDTO createLesson(@RequestBody @Valid LessonDTO lessonDTO) {
        return lessonService.createLesson(lessonDTO);
    }

    @GetMapping("/{lessonId}")
    @Operation(summary = "Get lesson by ID")
    public LessonDTO getLessonById(@PathVariable @NotNull UUID lessonId) {
        return lessonService.getLessonById(lessonId);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get all lessons for a course")
    public List<LessonDTO> getLessonsByCourseId(@PathVariable @NotNull UUID courseId) {
        return lessonService.getLessonsByCourseId(courseId);
    }

    @PutMapping("/{lessonId}")
    @Operation(summary = "Update a lesson")
    public LessonDTO updateLesson(
            @PathVariable @NotNull UUID lessonId,
            @RequestBody @Valid LessonDTO lessonDTO) {
        return lessonService.updateLesson(lessonId, lessonDTO);
    }

    @DeleteMapping("/{lessonId}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a lesson")
    public void deleteLesson(@PathVariable @NotNull UUID lessonId) {
        lessonService.deleteLesson(lessonId);
    }
}
