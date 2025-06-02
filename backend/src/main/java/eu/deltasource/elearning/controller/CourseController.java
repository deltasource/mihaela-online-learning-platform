package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Operation(summary = "Get all courses or courses by instructor email")
    public ResponseEntity<List<CourseDTO>> getAllCourses(@RequestParam(required = false) String instructorEmail) {
        List<CourseDTO> courses;
        if (instructorEmail != null && !instructorEmail.isEmpty()) {
            System.out.println("Controller: Fetching courses for instructor email: " + instructorEmail);
            courses = courseService.getCoursesByInstructorEmail(instructorEmail);
            System.out.println("Controller: Found " + courses.size() + " courses for instructor email: " + instructorEmail);
        } else {
            courses = courseService.getAllCourses();
        }
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable UUID id) {
        CourseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @PostMapping
    @Operation(summary = "Create a new course")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing course")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable UUID id, @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a course")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search courses by title or description")
    public ResponseEntity<List<CourseDTO>> searchCourses(@RequestParam String query) {
        List<CourseDTO> courses = courseService.searchCourses(query);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get courses by category")
    public ResponseEntity<List<CourseDTO>> getCoursesByCategory(@PathVariable String category) {
        List<CourseDTO> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Get courses by instructor ID")
    public ResponseEntity<List<CourseDTO>> getCoursesByInstructorId(@PathVariable UUID instructorId) {
        List<CourseDTO> courses = courseService.getCoursesByInstructorId(instructorId);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/fix-orphan-courses")
    @Operation(summary = "Fix courses without instructors")
    public ResponseEntity<String> fixOrphanCourses(@RequestParam String instructorEmail) {
        String result = courseService.fixOrphanCourses(instructorEmail);
        return ResponseEntity.ok(result);
    }
}
