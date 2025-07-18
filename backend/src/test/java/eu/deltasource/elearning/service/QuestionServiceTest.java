package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.QuestionDTO;
import eu.deltasource.elearning.exception.QuestionNotFoundException;
import eu.deltasource.elearning.exception.QuizNotFoundException;
import eu.deltasource.elearning.model.Question;
import eu.deltasource.elearning.model.Quiz;
import eu.deltasource.elearning.repository.QuestionRepository;
import eu.deltasource.elearning.repository.QuizRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void givenValidQuestionDTO_whenCreateQuestion_thenSavesAndReturnsDTO() {
        // Given
        UUID quizId = UUID.randomUUID();
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuizId(quizId);
        questionDTO.setQuestion("What is Java?");
        questionDTO.setQuestionType("SINGLE");
        questionDTO.setPoints(5);
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(questionRepository.save(any(Question.class))).thenAnswer(inv -> {
            Question q = inv.getArgument(0);
            q.setId(UUID.randomUUID());
            return q;
        });

        // When
        QuestionDTO result = questionService.createQuestion(questionDTO);

        // Then
        assertEquals("What is Java?", result.getQuestion());
        assertEquals("SINGLE", result.getQuestionType());
        assertEquals(5, result.getPoints());
        assertEquals(quizId, result.getQuizId());
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    void givenNonExistentQuiz_whenCreateQuestion_thenThrowsQuizNotFoundException() {
        // Given
        UUID quizId = UUID.randomUUID();
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuizId(quizId);
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuizNotFoundException.class, () -> questionService.createQuestion(questionDTO));
    }

    @Test
    void givenExistingQuestionId_whenGetQuestionById_thenReturnsDTO() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID quizId = UUID.randomUUID();
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        Question question = new Question();
        question.setId(questionId);
        question.setQuiz(quiz);
        question.setQuestion("Q?");
        question.setQuestionType("MULTI");
        question.setPoints(10);
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // When
        QuestionDTO dto = questionService.getQuestionById(questionId);

        // Then
        assertEquals(questionId, dto.getId());
        assertEquals(quizId, dto.getQuizId());
        assertEquals("Q?", dto.getQuestion());
        assertEquals("MULTI", dto.getQuestionType());
        assertEquals(10, dto.getPoints());
    }

    @Test
    void givenNonExistentQuestionId_whenGetQuestionById_thenThrowsQuestionNotFoundException() {
        // Given
        UUID questionId = UUID.randomUUID();
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuestionNotFoundException.class, () -> questionService.getQuestionById(questionId));
    }

    @Test
    void givenQuizId_whenGetQuestionsByQuizId_thenReturnsList() {
        // Given
        UUID quizId = UUID.randomUUID();
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        Question q1 = new Question();
        q1.setId(UUID.randomUUID());
        q1.setQuiz(quiz);
        q1.setQuestion("Q1");
        q1.setQuestionType("SINGLE");
        q1.setPoints(1);
        Question q2 = new Question();
        q2.setId(UUID.randomUUID());
        q2.setQuiz(quiz);
        q2.setQuestion("Q2");
        q2.setQuestionType("MULTI");
        q2.setPoints(2);
        when(questionRepository.findByQuizId(quizId)).thenReturn(Arrays.asList(q1, q2));

        // When
        List<QuestionDTO> result = questionService.getQuestionsByQuizId(quizId);

        // Then
        assertEquals(2, result.size());
        assertEquals("Q1", result.get(0).getQuestion());
        assertEquals("Q2", result.get(1).getQuestion());
    }

    @Test
    void givenValidUpdate_whenUpdateQuestion_thenUpdatesAndReturnsDTO() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID quizId = UUID.randomUUID();
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        Question existing = new Question();
        existing.setId(questionId);
        existing.setQuiz(quiz);
        existing.setQuestion("Old?");
        existing.setQuestionType("SINGLE");
        existing.setPoints(1);
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuizId(quizId);
        questionDTO.setQuestion("New?");
        questionDTO.setQuestionType("MULTI");
        questionDTO.setPoints(10);
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(existing));
        when(questionRepository.save(any(Question.class))).thenAnswer(i -> i.getArgument(0));

        // When
        QuestionDTO result = questionService.updateQuestion(questionId, questionDTO);

        // Then
        assertEquals("New?", result.getQuestion());
        assertEquals("MULTI", result.getQuestionType());
        assertEquals(10, result.getPoints());
        verify(questionRepository).save(existing);
    }

    @Test
    void givenUpdateWithQuizChange_whenUpdateQuestion_thenUpdatesQuiz() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID oldQuizId = UUID.randomUUID();
        UUID newQuizId = UUID.randomUUID();
        Quiz oldQuiz = new Quiz();
        oldQuiz.setId(oldQuizId);
        Quiz newQuiz = new Quiz();
        newQuiz.setId(newQuizId);
        Question existing = new Question();
        existing.setId(questionId);
        existing.setQuiz(oldQuiz);
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuizId(newQuizId);
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(existing));
        when(quizRepository.findById(newQuizId)).thenReturn(Optional.of(newQuiz));
        when(questionRepository.save(any(Question.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        QuestionDTO result = questionService.updateQuestion(questionId, questionDTO);

        // Then
        assertEquals(newQuizId, result.getQuizId());
        verify(questionRepository).save(existing);
    }

    @Test
    void givenUpdateWithNonExistentQuestion_whenUpdateQuestion_thenThrowsQuestionNotFoundException() {
        // Given
        UUID questionId = UUID.randomUUID();
        QuestionDTO questionDTO = new QuestionDTO();
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuestionNotFoundException.class, () -> questionService.updateQuestion(questionId, questionDTO));
    }

    @Test
    void givenUpdateWithNonExistentQuiz_whenUpdateQuestion_thenThrowsQuizNotFoundException() {
        // Given
        UUID questionId = UUID.randomUUID();
        UUID oldQuizId = UUID.randomUUID();
        UUID newQuizId = UUID.randomUUID();
        Quiz oldQuiz = new Quiz();
        oldQuiz.setId(oldQuizId);
        Question existing = new Question();
        existing.setId(questionId);
        existing.setQuiz(oldQuiz);
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuizId(newQuizId);
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(existing));
        when(quizRepository.findById(newQuizId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuizNotFoundException.class, () -> questionService.updateQuestion(questionId, questionDTO));
    }

    @Test
    void givenExistingQuestionId_whenDeleteQuestion_thenDeletesQuestion() {
        // Given
        UUID questionId = UUID.randomUUID();
        Question question = new Question();
        question.setId(questionId);
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // When
        questionService.deleteQuestion(questionId);

        // Then
        verify(questionRepository).delete(question);
    }

    @Test
    void givenNonExistentQuestionId_whenDeleteQuestion_thenThrowsQuestionNotFoundException() {
        // Given
        UUID questionId = UUID.randomUUID();
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuestionNotFoundException.class, () -> questionService.deleteQuestion(questionId));
    }
}
