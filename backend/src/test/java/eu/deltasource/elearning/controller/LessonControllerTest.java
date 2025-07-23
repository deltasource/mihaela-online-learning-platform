package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.LessonDTO;
import eu.deltasource.elearning.service.LessonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The @AutoConfigureMockMvc(addFilters = false) annotation is used in Spring Boot testing to disable the automatic addition of Spring Security filters when configuring MockMvc.
 * By default, when @AutoConfigureMockMvc is used, any registered filters (such as security filters) are applied to the MockMvc instance. Setting addFilters = false prevents these filters from being added, allowing tests to bypass security constraints and focus on controller logic without authentication or authorization requirements.
 **/
@WebMvcTest(LessonController.class)
@AutoConfigureMockMvc(addFilters = false)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidLessonId_whenGetLessonById_thenReturnsLessonDTO() throws Exception {
        // Given
        UUID lessonId = UUID.randomUUID();
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setId(lessonId);
        lessonDTO.setTitle("Intro to Java");
        when(lessonService.getLessonById(lessonId)).thenReturn(lessonDTO);

        // When
        var result = mockMvc.perform(get("/api/lessons/{lessonId}", lessonId));

        // Then
        result.andExpect(status().isOk());
        verify(lessonService, times(1)).getLessonById(lessonId);
    }

    @Test
    void givenValidCourseId_whenGetLessonsByCourseId_thenReturnsListOfLessonDTOs() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        List<LessonDTO> lessons = List.of(new LessonDTO(), new LessonDTO());
        when(lessonService.getLessonsByCourseId(courseId)).thenReturn(lessons);

        // When
        var result = mockMvc.perform(get("/api/lessons/course/{courseId}", courseId));

        // Then
        result.andExpect(status().isOk());
        verify(lessonService, times(1)).getLessonsByCourseId(courseId);
    }

    @Test
    void givenValidLessonId_whenDeleteLesson_thenReturnsNoContent() throws Exception {
        // Given
        UUID lessonId = UUID.randomUUID();
        doNothing().when(lessonService).deleteLesson(lessonId);

        // When
        var result = mockMvc.perform(delete("/api/lessons/{lessonId}", lessonId));

        // Then
        result.andExpect(status().isNoContent());
        verify(lessonService, times(1)).deleteLesson(lessonId);
    }
}
