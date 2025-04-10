package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Course Managment")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CourseDTO createCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.createCourse(courseDTO);
    }

    @GetMapping("/{courseId}")
    public CourseDTO getCourseById(@PathVariable UUID courseId) {
        return courseService.getCourseById(courseId);
    }

    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PutMapping("/{courseId}")
    public CourseDTO updateCourse(@PathVariable UUID courseId, @RequestBody CourseDTO courseDTO) {
        return courseService.updateCourse(courseId, courseDTO);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCourse(@PathVariable UUID courseId) {
        courseService.deleteCourse(courseId);
    }
}
