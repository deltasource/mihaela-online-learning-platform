package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.EnrollmentDTO;
import eu.deltasource.elearning.enums.EnrollmentStatus;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Enrollment;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.EnrollmentRepository;
import eu.deltasource.elearning.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private AnalyticsService analyticsService;
    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    void givenValidEnrollmentDTO_whenEnrollStudent_thenSavesAndReturnsDTO() {
        //Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        Student student = new Student();
        student.setId(studentId);
        student.setFirstName("John");
        student.setLastName("Doe");
        Course course = new Course();
        course.setId(courseId);
        course.setName("Math");
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setStudentId(studentId);
        dto.setCourseId(courseId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentAndCourse(student, course)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> {
            Enrollment e = inv.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        //When
        EnrollmentDTO result = enrollmentService.enrollStudent(dto);

        //Then
        assertEquals(studentId, result.getStudentId());
        assertEquals(courseId, result.getCourseId());
        assertEquals(EnrollmentStatus.ACTIVE, result.getStatus());
        verify(analyticsService).trackCourseEnrollment(studentId, courseId);
    }

    @Test
    void givenNonExistentStudent_whenEnrollStudent_thenThrowsException() {
        // Given
        UUID studentId = UUID.randomUUID();
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setStudentId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> enrollmentService.enrollStudent(dto));
    }

    @Test
    void givenNonExistentCourse_whenEnrollStudent_thenThrowsException() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        Student student = new Student();
        student.setId(studentId);
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setStudentId(studentId);
        dto.setCourseId(courseId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> enrollmentService.enrollStudent(dto));
    }

    @Test
    void givenAlreadyEnrolled_whenEnrollStudent_thenThrowsException() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        Student student = new Student();
        student.setId(studentId);
        Course course = new Course();
        course.setId(courseId);
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setStudentId(studentId);
        dto.setCourseId(courseId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentAndCourse(student, course)).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> enrollmentService.enrollStudent(dto));
    }

    @Test
    void givenStudentId_whenGetStudentEnrollments_thenReturnsList() {
        // Given
        UUID studentId = UUID.randomUUID();
        Student student = new Student();
        student.setId(studentId);
        Course course = new Course();
        course.setId(UUID.randomUUID());
        course.setName("Math");
        Enrollment enrollment = new Enrollment();
        enrollment.setId(UUID.randomUUID());
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(enrollmentRepository.findByStudent(student)).thenReturn(List.of(enrollment));

        // When
        List<EnrollmentDTO> result = enrollmentService.getStudentEnrollments(studentId);

        // Then
        assertEquals(1, result.size());
        assertEquals(studentId, result.get(0).getStudentId());
    }

    @Test
    void givenNonExistentStudent_whenGetStudentEnrollments_thenThrowsException() {
        // Given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> enrollmentService.getStudentEnrollments(studentId));
    }

    @Test
    void givenCourseId_whenGetCourseEnrollments_thenReturnsList() {
        // Given
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        course.setName("Math");
        Student student = new Student();
        student.setId(UUID.randomUUID());
        student.setFirstName("Jane");
        student.setLastName("Smith");
        Enrollment enrollment = new Enrollment();
        enrollment.setId(UUID.randomUUID());
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByCourse(course)).thenReturn(List.of(enrollment));

        // When
        List<EnrollmentDTO> result = enrollmentService.getCourseEnrollments(courseId);

        // Then
        assertEquals(1, result.size());
        assertEquals(courseId, result.get(0).getCourseId());
    }

    @Test
    void givenNonExistentCourse_whenGetCourseEnrollments_thenThrowsException() {
        // Given
        UUID courseId = UUID.randomUUID();
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> enrollmentService.getCourseEnrollments(courseId));
    }

    @Test
    void givenValidStatus_whenUpdateEnrollmentStatus_thenUpdatesAndReturnsDTO() {
        // Given
        UUID enrollmentId = UUID.randomUUID();
        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentId);
        Student student = new Student();
        student.setId(UUID.randomUUID());
        enrollment.setStudent(student);
        Course course = new Course();
        course.setId(UUID.randomUUID());
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        EnrollmentDTO result = enrollmentService.updateEnrollmentStatus(enrollmentId, "COMPLETED");

        // Then
        assertEquals(EnrollmentStatus.COMPLETED, result.getStatus());
        assertNotNull(result.getCompletedAt());
    }

    @Test
    void givenNonExistentEnrollment_whenUpdateEnrollmentStatus_thenThrowsException() {
        // Given
        UUID enrollmentId = UUID.randomUUID();
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> enrollmentService.updateEnrollmentStatus(enrollmentId, "ACTIVE"));
    }
}
