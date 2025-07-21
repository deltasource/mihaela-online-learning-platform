package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.EnrollmentDTO;
import eu.deltasource.elearning.enums.EnrollmentStatus;
import eu.deltasource.elearning.exception.EnrollmentNotPossibleException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Enrollment;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.EnrollmentRepository;
import eu.deltasource.elearning.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AnalyticsService analyticsService;

    @Transactional
    public EnrollmentDTO enrollStudent(EnrollmentDTO enrollmentDTO) {

        log.info("Enrollment DTO: {}", enrollmentDTO);
        Student student = studentRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new EnrollmentNotPossibleException("Student not found"));

        log.info("Student enrolled: {}", student);
        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new EnrollmentNotPossibleException("Course not found"));

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            log.warn("Enrollment attempt for student {} in course {} that is already enrolled", student.getId(), course.getId());
            throw new EnrollmentNotPossibleException("Student is already enrolled in this course");
        }

        log.info("Enrolling student {} in course {}", student.getId(), course.getId());
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment = enrollmentRepository.save(enrollment);
        analyticsService.trackCourseEnrollment(student.getId(), course.getId());
        return mapToDTO(enrollment);
    }

    public List<EnrollmentDTO> getStudentEnrollments(UUID studentId) {
        log.info("Fetching enrollments for student ID: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EnrollmentNotPossibleException("Student not found"));

        return enrollmentRepository.findByStudent(student).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getCourseEnrollments(UUID courseId) {
        log.info("Fetching enrollments for course ID: {}", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EnrollmentNotPossibleException("Course not found"));

        log.info("Course found: {}", course);
        return enrollmentRepository.findByCourse(course).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentDTO updateEnrollmentStatus(UUID enrollmentId, String status) {
        log.info("Updating enrollment status for ID: {} to {}", enrollmentId, status);
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EnrollmentNotPossibleException("Enrollment not found"));

        log.info("Enrollment found: {}", enrollment);
        EnrollmentStatus newStatus = EnrollmentStatus.valueOf(status.toUpperCase());
        enrollment.setStatus(newStatus);

        if (newStatus == EnrollmentStatus.COMPLETED) {
            enrollment.setCompletedAt(LocalDateTime.now());
        }

        enrollment = enrollmentRepository.save(enrollment);
        return mapToDTO(enrollment);
    }

    private EnrollmentDTO mapToDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setStatus(enrollment.getStatus());
        dto.setEnrolledAt(enrollment.getEnrolledAt());
        dto.setCompletedAt(enrollment.getCompletedAt());
        dto.setFinalGrade(enrollment.getFinalGrade());
        dto.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        dto.setCourseName(enrollment.getCourse().getName());
        return dto;
    }
}
