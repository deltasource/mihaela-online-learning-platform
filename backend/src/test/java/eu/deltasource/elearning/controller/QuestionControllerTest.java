package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.QuestionDTO;
import eu.deltasource.elearning.service.QuestionService;
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
class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    @Test
    void givenValidQuestionDTO_whenCreateQuestion_thenReturnQuestionDTO() {
        // Given
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion("What is Java?");
        questionDTO.setQuizId(UUID.randomUUID());
        when(questionService.createQuestion(questionDTO)).thenReturn(questionDTO);

        // When
        QuestionDTO response = questionController.createQuestion(questionDTO);

        // Then
        assertEquals(questionDTO, response);
        verify(questionService, times(1)).createQuestion(questionDTO);
    }

    @Test
    void givenValidQuestionId_whenGetQuestionById_thenReturnQuestionDTO() {
        // Given
        UUID questionId = UUID.randomUUID();
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(questionId);
        questionDTO.setQuestion("What is Java?");
        when(questionService.getQuestionById(questionId)).thenReturn(questionDTO);

        // When
        QuestionDTO response = questionController.getQuestionById(questionId);

        // Then
        assertEquals(questionDTO, response);
        verify(questionService, times(1)).getQuestionById(questionId);
    }

    @Test
    void givenValidQuizId_whenGetQuestionsByQuizId_thenReturnListOfQuestionDTOs() {
        // Given
        UUID quizId = UUID.randomUUID();
        List<QuestionDTO> questions = List.of(new QuestionDTO(), new QuestionDTO());
        when(questionService.getQuestionsByQuizId(quizId)).thenReturn(questions);

        // When
        List<QuestionDTO> response = questionController.getQuestionsByQuizId(quizId);

        // Then
        assertEquals(questions, response);
        verify(questionService, times(1)).getQuestionsByQuizId(quizId);
    }

    @Test
    void givenValidQuestionIdAndQuestionDTO_whenUpdateQuestion_thenReturnUpdatedQuestionDTO() {
        // Given
        UUID questionId = UUID.randomUUID();
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuestion("Updated Question");
        questionDTO.setQuizId(UUID.randomUUID());
        when(questionService.updateQuestion(questionId, questionDTO)).thenReturn(questionDTO);

        // When
        QuestionDTO response = questionController.updateQuestion(questionId, questionDTO);

        // Then
        assertEquals(questionDTO, response);
        verify(questionService, times(1)).updateQuestion(questionId, questionDTO);
    }

    @Test
    void givenValidQuestionId_whenDeleteQuestion_thenVerifyServiceCalled() {
        // Given
        UUID questionId = UUID.randomUUID();
        doNothing().when(questionService).deleteQuestion(questionId);

        // When
        questionController.deleteQuestion(questionId);

        // Then
        verify(questionService, times(1)).deleteQuestion(questionId);
    }
}
