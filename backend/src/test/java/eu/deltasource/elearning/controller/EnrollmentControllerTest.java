package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.EnrollmentDTO;
import eu.deltasource.elearning.enums.EnrollmentStatus;
import eu.deltasource.elearning.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @InjectMocks
    private EnrollmentController enrollmentController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void givenStudentAndCourseExist_whenEnrollStudent_thenStudentIsEnrolledSuccessfully() {
        // Given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setStudentId(UUID.randomUUID());
        enrollmentDTO.setCourseId(UUID.randomUUID());
        enrollmentDTO.setStatus(EnrollmentStatus.ACTIVE);

        when(enrollmentService.enrollStudent(enrollmentDTO)).thenReturn(enrollmentDTO);

        // When
        ResponseEntity<EnrollmentDTO> response = enrollmentController.enrollStudent(enrollmentDTO);

        // Then
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(enrollmentDTO, response.getBody());
        verify(enrollmentService, times(1)).enrollStudent(enrollmentDTO);
    }

    @Test
    void givenStudentExists_whenGetStudentEnrollments_thenEnrollmentsAreReturnedSuccessfully() {
        // Given
        UUID studentId = UUID.randomUUID();
        List<EnrollmentDTO> enrollments = List.of(new EnrollmentDTO(), new EnrollmentDTO());
        when(enrollmentService.getStudentEnrollments(studentId)).thenReturn(enrollments);

        // When
        ResponseEntity<List<EnrollmentDTO>> response = enrollmentController.getStudentEnrollments(studentId);

        // Then
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(enrollments, response.getBody());
        verify(enrollmentService, times(1)).getStudentEnrollments(studentId);
    }

    @Test
    void givenCourseExists_whenGetCourseEnrollments_thenEnrollmentsAreReturnedSuccessfully() {
        // Given
        UUID courseId = UUID.randomUUID();
        List<EnrollmentDTO> enrollments = List.of(new EnrollmentDTO(), new EnrollmentDTO());
        when(enrollmentService.getCourseEnrollments(courseId)).thenReturn(enrollments);

        // When
        ResponseEntity<List<EnrollmentDTO>> response = enrollmentController.getCourseEnrollments(courseId);

        // Then
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(enrollments, response.getBody());
        verify(enrollmentService, times(1)).getCourseEnrollments(courseId);
    }

    @Test
    void givenEnrollmentExists_whenUpdateEnrollmentStatus_thenStatusIsUpdatedSuccessfully() {
        // Given
        UUID enrollmentId = UUID.randomUUID();
        String status = "COMPLETED";
        EnrollmentDTO updatedEnrollment = new EnrollmentDTO();
        updatedEnrollment.setStatus(EnrollmentStatus.COMPLETED);
        when(enrollmentService.updateEnrollmentStatus(enrollmentId, status)).thenReturn(updatedEnrollment);

        // When
        ResponseEntity<EnrollmentDTO> response = enrollmentController.updateEnrollmentStatus(enrollmentId, status);

        // Then
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals(updatedEnrollment, response.getBody());
        verify(enrollmentService, times(1)).updateEnrollmentStatus(enrollmentId, status);
    }
}
