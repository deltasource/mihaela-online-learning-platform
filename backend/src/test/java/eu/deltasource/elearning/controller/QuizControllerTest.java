package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.QuizDTO;
import eu.deltasource.elearning.service.QuizService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuizController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuizService quizService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidQuizId_whenGetQuizById_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID quizId = UUID.randomUUID();
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quizId);
        quizDTO.setTitle("Java Quiz");
        quizDTO.setCourseId(UUID.randomUUID());
        when(quizService.getQuizById(quizId)).thenReturn(quizDTO);

        // When
        var result = mockMvc.perform(get("/api/quizzes/{quizId}", quizId));

        // Then
        result.andExpect(status().isOk());
        verify(quizService, times(1)).getQuizById(eq(quizId));
    }

    @Test
    void givenValidCourseId_whenGetQuizzesByCourseId_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        List<QuizDTO> quizzes = List.of(new QuizDTO(), new QuizDTO());
        when(quizService.getQuizzesByCourseId(courseId)).thenReturn(quizzes);

        // When
        var result = mockMvc.perform(get("/api/quizzes/course/{courseId}", courseId));

        // Then
        result.andExpect(status().isOk());
        verify(quizService, times(1)).getQuizzesByCourseId(eq(courseId));
    }

    @Test
    void givenValidQuizId_whenDeleteQuiz_thenReturnsNoContentAndServiceCalled() throws Exception {
        // Given
        UUID quizId = UUID.randomUUID();
        doNothing().when(quizService).deleteQuiz(quizId);

        // When
        var result = mockMvc.perform(delete("/api/quizzes/{quizId}", quizId));

        // Then
        result.andExpect(status().isNoContent());
        verify(quizService, times(1)).deleteQuiz(eq(quizId));
    }

    @Test
    void givenInvalidQuizDTO_whenCreateQuiz_thenReturnsBadRequest() throws Exception {
        // Given
        QuizDTO quizDTO = new QuizDTO();

        // When
        var result = mockMvc.perform(post("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quizDTO)));

        // Then
        result.andExpect(status().isBadRequest());
    }
}
