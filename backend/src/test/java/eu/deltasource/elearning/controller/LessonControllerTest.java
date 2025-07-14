package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.LessonDTO;
import eu.deltasource.elearning.service.LessonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    @Test
    void givenValidLessonDTO_whenCreateLesson_thenReturnLessonDTO() {
        // Given
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setTitle("Introduction to Java");
        lessonDTO.setDescription("Learn the basics of Java programming");
        when(lessonService.createLesson(lessonDTO)).thenReturn(lessonDTO);

        // When
        LessonDTO response = lessonController.createLesson(lessonDTO);

        // Then
        assertEquals(lessonDTO, response);
        verify(lessonService, times(1)).createLesson(lessonDTO);
    }

    @Test
    void givenValidLessonId_whenGetLessonById_thenReturnLessonDTO() {
        // Given
        UUID lessonId = UUID.randomUUID();
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setId(lessonId);
        lessonDTO.setTitle("Introduction to Java");
        when(lessonService.getLessonById(lessonId)).thenReturn(lessonDTO);

        // When
        LessonDTO response = lessonController.getLessonById(lessonId);

        // Then
        assertEquals(lessonDTO, response);
        verify(lessonService, times(1)).getLessonById(lessonId);
    }

    @Test
    void givenValidCourseId_whenGetLessonsByCourseId_thenReturnListOfLessonDTOs() {
        // Given
        UUID courseId = UUID.randomUUID();
        List<LessonDTO> lessons = List.of(new LessonDTO(), new LessonDTO());
        when(lessonService.getLessonsByCourseId(courseId)).thenReturn(lessons);

        // When
        List<LessonDTO> response = lessonController.getLessonsByCourseId(courseId);

        // Then
        assertEquals(lessons, response);
        verify(lessonService, times(1)).getLessonsByCourseId(courseId);
    }

    @Test
    void givenValidLessonIdAndLessonDTO_whenUpdateLesson_thenReturnUpdatedLessonDTO() {
        // Given
        UUID lessonId = UUID.randomUUID();
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setTitle("Advanced Java");
        lessonDTO.setDescription("Deep dive into Java programming");
        when(lessonService.updateLesson(lessonId, lessonDTO)).thenReturn(lessonDTO);

        // When
        LessonDTO response = lessonController.updateLesson(lessonId, lessonDTO);

        // Then
        assertEquals(lessonDTO, response);
        verify(lessonService, times(1)).updateLesson(lessonId, lessonDTO);
    }

    @Test
    void givenValidLessonId_whenDeleteLesson_thenVerifyServiceCalled() {
        // Given
        UUID lessonId = UUID.randomUUID();
        doNothing().when(lessonService).deleteLesson(lessonId);

        // When
        lessonController.deleteLesson(lessonId);

        // Then
        verify(lessonService, times(1)).deleteLesson(lessonId);
    }
}
