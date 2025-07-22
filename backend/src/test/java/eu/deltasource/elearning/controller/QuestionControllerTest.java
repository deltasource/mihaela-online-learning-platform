package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.QuestionDTO;
import eu.deltasource.elearning.service.QuestionService;
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

@WebMvcTest(QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidQuestionId_whenGetQuestionById_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID questionId = UUID.randomUUID();
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(questionId);
        questionDTO.setQuestion("What is Java?");
        questionDTO.setQuizId(UUID.randomUUID());
        when(questionService.getQuestionById(questionId)).thenReturn(questionDTO);

        // When
        var result = mockMvc.perform(get("/api/questions/{questionId}", questionId));

        // Then
        result.andExpect(status().isOk());
        verify(questionService, times(1)).getQuestionById(eq(questionId));
    }

    @Test
    void givenValidQuizId_whenGetQuestionsByQuizId_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID quizId = UUID.randomUUID();
        List<QuestionDTO> questions = List.of(new QuestionDTO(), new QuestionDTO());
        when(questionService.getQuestionsByQuizId(quizId)).thenReturn(questions);

        // When
        var result = mockMvc.perform(get("/api/questions/quiz/{quizId}", quizId));

        // Then
        result.andExpect(status().isOk());
        verify(questionService, times(1)).getQuestionsByQuizId(eq(quizId));
    }

    @Test
    void givenValidQuestionId_whenDeleteQuestion_thenReturnsNoContentAndServiceCalled() throws Exception {
        // Given
        UUID questionId = UUID.randomUUID();
        doNothing().when(questionService).deleteQuestion(questionId);

        // When
        var result = mockMvc.perform(delete("/api/questions/{questionId}", questionId));

        // Then
        result.andExpect(status().isNoContent());
        verify(questionService, times(1)).deleteQuestion(eq(questionId));
    }

    @Test
    void givenInvalidQuestionDTO_whenCreateQuestion_thenReturnsBadRequest() throws Exception {
        // Given
        QuestionDTO questionDTO = new QuestionDTO(); // missing required fields

        // When
        var result = mockMvc.perform(post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionDTO)));

        // Then
        result.andExpect(status().isBadRequest());
    }
}
