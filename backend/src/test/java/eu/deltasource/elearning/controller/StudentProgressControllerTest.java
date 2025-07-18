package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.StudentProgressDTO;
import eu.deltasource.elearning.service.StudentProgressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentProgressControllerTest {

    @Mock
    private StudentProgressService studentProgressService;

    @InjectMocks
    private StudentProgressController studentProgressController;

    @Test
    void givenValidStudentIdAndCourseId_whenGetProgressPercentage_thenReturnStudentProgressDTO() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        StudentProgressDTO progressDTO = new StudentProgressDTO();
        progressDTO.setProgressPercentage(75.0);
        when(studentProgressService.getProgressPercentage(studentId, courseId)).thenReturn(progressDTO);

        // When
        StudentProgressDTO response = studentProgressController.getProgressPercentage(studentId, courseId);

        // Then
        assertEquals(progressDTO, response);
        verify(studentProgressService, times(1)).getProgressPercentage(studentId, courseId);
    }

    @Test
    void givenValidStudentIdCourseIdAndVideoId_whenUpdateProgress_thenVerifyServiceCalled() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();
        doNothing().when(studentProgressService).updateProgress(studentId, courseId, videoId);

        // When
        studentProgressController.updateProgress(studentId, courseId, videoId);

        // Then
        verify(studentProgressService, times(1)).updateProgress(studentId, courseId, videoId);
    }
}
