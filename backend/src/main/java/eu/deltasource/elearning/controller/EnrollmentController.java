package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.EnrollmentDTO;
import eu.deltasource.elearning.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Enrollment Management", description = "Course enrollment operations")
@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(summary = "Enroll student in course")
    @PostMapping
    public ResponseEntity<EnrollmentDTO> enrollStudent(@Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        log.info("Enrolling student with ID: {} in course with ID: {}", enrollmentDTO.getStudentId(), enrollmentDTO.getCourseId());
        return ResponseEntity.ok(enrollmentService.enrollStudent(enrollmentDTO));
    }

    @Operation(summary = "Get student enrollments")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getStudentEnrollments(@PathVariable UUID studentId) {
        log.info("Retrieving enrollments for student with ID: {}", studentId);
        return ResponseEntity.ok(enrollmentService.getStudentEnrollments(studentId));
    }

    @Operation(summary = "Get course enrollments")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getCourseEnrollments(@PathVariable UUID courseId) {
        log.info("Retrieving enrollments for course with ID: {}", courseId);
        return ResponseEntity.ok(enrollmentService.getCourseEnrollments(courseId));
    }

    @Operation(summary = "Update enrollment status")
    @PutMapping("/{enrollmentId}/status")
    public ResponseEntity<EnrollmentDTO> updateEnrollmentStatus(
            @PathVariable UUID enrollmentId,
            @RequestParam String status) {
        log.info("Updating enrollment status for ID: {} to status: {}", enrollmentId, status);
        return ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(enrollmentId, status));
    }
}
