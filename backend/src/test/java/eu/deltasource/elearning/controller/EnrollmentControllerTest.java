package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.EnrollmentDTO;
import eu.deltasource.elearning.enums.EnrollmentStatus;
import eu.deltasource.elearning.service.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrollmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidEnrollmentDTO_whenEnrollStudent_thenReturnsEnrollment() throws Exception {
        // Given
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setStudentId(UUID.randomUUID());
        enrollmentDTO.setCourseId(UUID.randomUUID());
        enrollmentDTO.setStatus(EnrollmentStatus.ACTIVE);
        when(enrollmentService.enrollStudent(any(EnrollmentDTO.class))).thenReturn(enrollmentDTO);

        // When & Then
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentDTO)))
                .andExpect(status().isOk());
        verify(enrollmentService, times(1)).enrollStudent(any(EnrollmentDTO.class));
    }

    @Test
    void givenStudentId_whenGetStudentEnrollments_thenReturnsList() throws Exception {
        // Given
        UUID studentId = UUID.randomUUID();
        List<EnrollmentDTO> enrollments = List.of(new EnrollmentDTO(), new EnrollmentDTO());
        when(enrollmentService.getStudentEnrollments(studentId)).thenReturn(enrollments);

        // When & Then
        mockMvc.perform(get("/api/enrollments/student/{studentId}", studentId))
                .andExpect(status().isOk());
        verify(enrollmentService, times(1)).getStudentEnrollments(studentId);
    }

    @Test
    void givenCourseId_whenGetCourseEnrollments_thenReturnsList() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        List<EnrollmentDTO> enrollments = List.of(new EnrollmentDTO(), new EnrollmentDTO());
        when(enrollmentService.getCourseEnrollments(courseId)).thenReturn(enrollments);

        // When & Then
        mockMvc.perform(get("/api/enrollments/course/{courseId}", courseId))
                .andExpect(status().isOk());
        verify(enrollmentService, times(1)).getCourseEnrollments(courseId);
    }

    @Test
    void givenEnrollmentIdAndStatus_whenUpdateEnrollmentStatus_thenReturnsUpdatedEnrollment() throws Exception {
        // Given
        UUID enrollmentId = UUID.randomUUID();
        String status = "COMPLETED";
        EnrollmentDTO updatedEnrollment = new EnrollmentDTO();
        updatedEnrollment.setStatus(EnrollmentStatus.COMPLETED);
        when(enrollmentService.updateEnrollmentStatus(eq(enrollmentId), eq(status))).thenReturn(updatedEnrollment);

        // When & Then
        mockMvc.perform(put("/api/enrollments/{enrollmentId}/status", enrollmentId )
                        .param("status", status))
                .andExpect(status().isOk());
        verify(enrollmentService, times(1)).updateEnrollmentStatus(enrollmentId, status);
    }
}
