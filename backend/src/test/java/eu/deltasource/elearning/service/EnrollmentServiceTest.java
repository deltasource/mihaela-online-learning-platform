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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        // Given
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

        // When
        EnrollmentDTO result = enrollmentService.enrollStudent(dto);

        // Then
        assertEquals(studentId, result.getStudentId());
        assertEquals(courseId, result.getCourseId());
        assertEquals(EnrollmentStatus.ACTIVE, result.getStatus());
        verify(analyticsService).trackCourseEnrollment(studentId, courseId);
    }

    @Test
    void givenNonExistentStudent_whenEnrollStudent_thenThrowsCustomException() {
        // Given
        UUID studentId = UUID.randomUUID();
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setStudentId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When & Then
        EnrollmentNotPossibleException exception = assertThrows(EnrollmentNotPossibleException.class,
                () -> enrollmentService.enrollStudent(dto));
        assertEquals("Student not found", exception.getMessage());
        assertEquals(EnrollmentNotPossibleException.class, exception.getClass());
    }

    @Test
    void givenNonExistentCourse_whenEnrollStudent_thenThrowsCustomException() {
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
        EnrollmentNotPossibleException exception = assertThrows(EnrollmentNotPossibleException.class,
                () -> enrollmentService.enrollStudent(dto));
        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    void givenAlreadyEnrolled_whenEnrollStudent_thenThrowsCustomException() {
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
        EnrollmentNotPossibleException exception = assertThrows(EnrollmentNotPossibleException.class,
                () -> enrollmentService.enrollStudent(dto));
        assertEquals("Student is already enrolled in this course", exception.getMessage());
    }

    @Test
    void givenNonExistentStudent_whenGetStudentEnrollments_thenThrowsCustomException() {
        // Given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When & Then
        EnrollmentNotPossibleException exception = assertThrows(EnrollmentNotPossibleException.class,
                () -> enrollmentService.getStudentEnrollments(studentId));
        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void givenNonExistentCourse_whenGetCourseEnrollments_thenThrowsCustomException() {
        // Given
        UUID courseId = UUID.randomUUID();
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        EnrollmentNotPossibleException exception = assertThrows(EnrollmentNotPossibleException.class,
                () -> enrollmentService.getCourseEnrollments(courseId));
        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    void givenNonExistentEnrollment_whenUpdateEnrollmentStatus_thenThrowsCustomException() {
        // Given
        UUID enrollmentId = UUID.randomUUID();
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        // When & Then
        EnrollmentNotPossibleException exception = assertThrows(EnrollmentNotPossibleException.class,
                () -> enrollmentService.updateEnrollmentStatus(enrollmentId, "ACTIVE"));
        assertEquals("Enrollment not found", exception.getMessage());
    }
}
