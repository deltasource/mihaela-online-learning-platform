package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.StudentProgressDTO;
import eu.deltasource.elearning.service.StudentProgressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentProgressController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentProgressService studentProgressService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidStudentIdAndCourseId_whenGetProgressPercentage_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        StudentProgressDTO progressDTO = new StudentProgressDTO();
        progressDTO.setProgressPercentage(75.0);

        when(studentProgressService.getProgressPercentage(studentId, courseId)).thenReturn(progressDTO);

        // When
        var result = mockMvc.perform(get("/students/progress/v1/{studentId}/courses/{courseId}", studentId, courseId));

        // Then
        result.andExpect(status().isOk());
        verify(studentProgressService, times(1)).getProgressPercentage(eq(studentId), eq(courseId));
    }

    @Test
    void givenValidIds_whenUpdateProgress_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();
        doNothing().when(studentProgressService).updateProgress(studentId, courseId, videoId);

        // When
        var result = mockMvc.perform(post("/students/progress/v1/{studentId}/courses/{courseId}/videos/{videoId}/update",
                studentId, courseId, videoId));

        // Then
        result.andExpect(status().isOk());
        verify(studentProgressService, times(1)).updateProgress(eq(studentId), eq(courseId), eq(videoId));
    }

    @Test
    void givenInvalidUUID_whenGetProgressPercentage_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(get("/students/progress/v1/{studentId}/courses/{courseId}", "invalid-uuid", "invalid-uuid"));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidUUID_whenUpdateProgress_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(post("/students/progress/v1/{studentId}/courses/{courseId}/videos/{videoId}/update",
                "invalid-uuid", "invalid-uuid", "invalid-uuid"));

        // Then
        result.andExpect(status().isBadRequest());
    }
}
