package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.QuizDTO;
import eu.deltasource.elearning.service.QuizService;
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
class QuizControllerTest {

    @Mock
    private QuizService quizService;

    @InjectMocks
    private QuizController quizController;

    @Test
    void givenValidQuizData_whenCreateQuiz_thenQuizIsSuccessfullyCreated() {
        // Given
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitle("Java Quiz");
        quizDTO.setCourseId(UUID.randomUUID());

        when(quizService.createQuiz(quizDTO)).thenReturn(quizDTO);

        // When
        QuizDTO response = quizController.createQuiz(quizDTO);

        // Then
        assertEquals(quizDTO, response);
        verify(quizService, times(1)).createQuiz(quizDTO);
    }

    @Test
    void givenQuizIdExists_whenGetQuizById_thenCorrectQuizIsReturned() {
        // Given
        UUID quizId = UUID.randomUUID();
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quizId);
        quizDTO.setTitle("Java Quiz");

        when(quizService.getQuizById(quizId)).thenReturn(quizDTO);

        // When
        QuizDTO response = quizController.getQuizById(quizId);

        // Then
        assertEquals(quizDTO, response);
        verify(quizService, times(1)).getQuizById(quizId);
    }

    @Test
    void givenCourseIdExists_whenGetQuizzesByCourseId_thenAllQuizzesAreReturned() {
        // Given
        UUID courseId = UUID.randomUUID();
        List<QuizDTO> quizzes = List.of(new QuizDTO(), new QuizDTO());
        when(quizService.getQuizzesByCourseId(courseId)).thenReturn(quizzes);

        // When
        List<QuizDTO> response = quizController.getQuizzesByCourseId(courseId);

        // Then
        assertEquals(quizzes, response);
        verify(quizService, times(1)).getQuizzesByCourseId(courseId);
    }

    @Test
    void givenQuizExists_whenUpdateQuiz_thenQuizIsUpdatedSuccessfully() {
        // Given
        UUID quizId = UUID.randomUUID();
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitle("Updated Quiz");
        quizDTO.setCourseId(UUID.randomUUID());

        when(quizService.updateQuiz(quizId, quizDTO)).thenReturn(quizDTO);

        // When
        QuizDTO response = quizController.updateQuiz(quizId, quizDTO);

        // Then
        assertEquals(quizDTO, response);
        verify(quizService, times(1)).updateQuiz(quizId, quizDTO);
    }

    @Test
    void givenQuizExists_whenDeleteQuiz_thenQuizIsDeletedSuccessfully() {
        // Given
        UUID quizId = UUID.randomUUID();
        doNothing().when(quizService).deleteQuiz(quizId);

        // When
        quizController.deleteQuiz(quizId);

        // Then
        verify(quizService, times(1)).deleteQuiz(quizId);
    }
}
