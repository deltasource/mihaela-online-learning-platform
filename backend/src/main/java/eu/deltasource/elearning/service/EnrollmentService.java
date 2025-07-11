package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.EnrollmentDTO;
import eu.deltasource.elearning.enums.EnrollmentStatus;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Enrollment;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.EnrollmentRepository;
import eu.deltasource.elearning.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final AnalyticsService analyticsService;

    @Transactional
    public EnrollmentDTO enrollStudent(EnrollmentDTO enrollmentDTO) {
        Student student = studentRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(enrollmentDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

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
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return enrollmentRepository.findByStudent(student).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getCourseEnrollments(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return enrollmentRepository.findByCourse(course).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentDTO updateEnrollmentStatus(UUID enrollmentId, String status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

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
