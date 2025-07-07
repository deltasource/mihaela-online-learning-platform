package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Course Management")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a new course")
    public CourseDTO createCourse(@RequestBody @Valid CourseDTO courseDTO) {
        return courseService.createCourse(courseDTO);
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "Get course by ID")
    public CourseDTO getCourseById(@PathVariable @NotNull UUID courseId) {
        return courseService.getCourseById(courseId);
    }

    @GetMapping
    @Operation(summary = "Get all courses")
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PutMapping("/{courseId}")
    @Operation(summary = "Update a course")
    public CourseDTO updateCourse(
            @PathVariable @NotNull UUID courseId,
            @RequestBody @Valid CourseDTO courseDTO) {
        return courseService.updateCourse(courseId, courseDTO);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a course")
    public void deleteCourse(@PathVariable @NotNull UUID courseId) {
        courseService.deleteCourse(courseId);
    }
}
