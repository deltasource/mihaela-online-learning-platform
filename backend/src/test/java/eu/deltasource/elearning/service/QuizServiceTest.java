package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.QuizDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.QuizNotFoundException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Quiz;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.QuizRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private QuizService quizService;

    @Test
    void givenValidQuizDTO_whenCreateQuiz_thenSavesAndReturnsDTO() {
        // Given
        UUID courseId = UUID.randomUUID();
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setCourseId(courseId);
        quizDTO.setTitle("Quiz 1");
        quizDTO.setDescription("Desc");
        quizDTO.setPassingScore(80);
        quizDTO.setTimeLimit(30);
        quizDTO.setOrder(1);
        quizDTO.setPublished(true);
        Course course = new Course();
        course.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(inv -> {
            Quiz q = inv.getArgument(0);
            q.setId(UUID.randomUUID());
            return q;
        });

        // When
        QuizDTO result = quizService.createQuiz(quizDTO);

        // Then
        assertEquals("Quiz 1", result.getTitle());
        assertEquals(courseId, result.getCourseId());
        assertTrue(result.isPublished());
        verify(quizRepository).save(any(Quiz.class));
    }

    @Test
    void givenNonExistentCourse_whenCreateQuiz_thenThrowsCourseNotFoundException() {
        // Given
        UUID courseId = UUID.randomUUID();
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setCourseId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> quizService.createQuiz(quizDTO));
    }

    @Test
    void givenExistingQuizId_whenGetQuizById_thenReturnsDTO() {
        // Given
        UUID quizId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setCourse(course);
        quiz.setTitle("Quiz");
        quiz.setDescription("Desc");
        quiz.setPassingScore(70);
        quiz.setTimeLimit(20);
        quiz.setOrder(2);
        quiz.setPublished(false);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        // When
        QuizDTO dto = quizService.getQuizById(quizId);

        // Then
        assertEquals(quizId, dto.getId());
        assertEquals(courseId, dto.getCourseId());
        assertEquals("Quiz", dto.getTitle());
    }

    @Test
    void givenNonExistentQuizId_whenGetQuizById_thenThrowsQuizNotFoundException() {
        // Given
        UUID quizId = UUID.randomUUID();
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuizNotFoundException.class, () -> quizService.getQuizById(quizId));
    }

    @Test
    void givenCourseId_whenGetQuizzesByCourseId_thenReturnsList() {
        // Given
        UUID courseId = UUID.randomUUID();
        Quiz quiz1 = new Quiz();
        quiz1.setId(UUID.randomUUID());
        Course course = new Course();
        course.setId(courseId);
        quiz1.setCourse(course);
        quiz1.setOrder(1);
        quiz1.setTitle("Q1");
        quiz1.setCreatedAt(LocalDateTime.now());
        quiz1.setUpdatedAt(LocalDateTime.now());
        Quiz quiz2 = new Quiz();
        quiz2.setId(UUID.randomUUID());
        quiz2.setCourse(course);
        quiz2.setOrder(2);
        quiz2.setTitle("Q2");
        quiz2.setCreatedAt(LocalDateTime.now());
        quiz2.setUpdatedAt(LocalDateTime.now());
        when(quizRepository.findByCourseIdOrderByOrderAsc(courseId)).thenReturn(Arrays.asList(quiz1, quiz2));

        // When
        List<QuizDTO> result = quizService.getQuizzesByCourseId(courseId);

        // Then
        assertEquals(2, result.size());
        assertEquals("Q1", result.get(0).getTitle());
        assertEquals("Q2", result.get(1).getTitle());
    }

    @Test
    void givenValidUpdate_whenUpdateQuiz_thenUpdatesAndReturnsDTO() {
        // Given
        UUID quizId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        Quiz existingQuiz = new Quiz();
        existingQuiz.setId(quizId);
        existingQuiz.setCourse(course);
        existingQuiz.setTitle("Old");
        existingQuiz.setDescription("OldDesc");
        existingQuiz.setPassingScore(50);
        existingQuiz.setTimeLimit(10);
        existingQuiz.setOrder(1);
        existingQuiz.setPublished(false);
        existingQuiz.setCreatedAt(LocalDateTime.now());
        existingQuiz.setUpdatedAt(LocalDateTime.now());
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setCourseId(courseId);
        quizDTO.setTitle("New");
        quizDTO.setDescription("NewDesc");
        quizDTO.setPassingScore(90);
        quizDTO.setTimeLimit(60);
        quizDTO.setOrder(2);
        quizDTO.setPublished(true);
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(existingQuiz));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        QuizDTO result = quizService.updateQuiz(quizId, quizDTO);

        // Then
        assertEquals("New", result.getTitle());
        assertEquals("NewDesc", result.getDescription());
        assertEquals(90, result.getPassingScore());
        assertTrue(result.isPublished());
        verify(quizRepository).save(existingQuiz);
    }

    @Test
    void givenUpdateWithCourseChange_whenUpdateQuiz_thenUpdatesCourse() {
        // Given
        UUID quizId = UUID.randomUUID();
        UUID oldCourseId = UUID.randomUUID();
        UUID newCourseId = UUID.randomUUID();
        Course oldCourse = new Course();
        oldCourse.setId(oldCourseId);
        Course newCourse = new Course();
        newCourse.setId(newCourseId);
        Quiz existingQuiz = new Quiz();
        existingQuiz.setId(quizId);
        existingQuiz.setCourse(oldCourse);
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setCourseId(newCourseId);
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(existingQuiz));
        when(courseRepository.findById(newCourseId)).thenReturn(Optional.of(newCourse));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        QuizDTO result = quizService.updateQuiz(quizId, quizDTO);

        // Then
        assertEquals(newCourseId, result.getCourseId());
        verify(quizRepository).save(existingQuiz);
    }

    @Test
    void givenUpdateWithNonExistentQuiz_whenUpdateQuiz_thenThrowsQuizNotFoundException() {
        // Given
        UUID quizId = UUID.randomUUID();
        QuizDTO quizDTO = new QuizDTO();
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuizNotFoundException.class, () -> quizService.updateQuiz(quizId, quizDTO));
    }

    @Test
    void givenUpdateWithNonExistentCourse_whenUpdateQuiz_thenThrowsCourseNotFoundException() {
        // Given
        UUID quizId = UUID.randomUUID();
        UUID oldCourseId = UUID.randomUUID();
        UUID newCourseId = UUID.randomUUID();
        Course oldCourse = new Course();
        oldCourse.setId(oldCourseId);
        Quiz existingQuiz = new Quiz();
        existingQuiz.setId(quizId);
        existingQuiz.setCourse(oldCourse);
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setCourseId(newCourseId);
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(existingQuiz));
        when(courseRepository.findById(newCourseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> quizService.updateQuiz(quizId, quizDTO));
    }

    @Test
    void givenExistingQuizId_whenDeleteQuiz_thenDeletesQuiz() {
        // Given
        UUID quizId = UUID.randomUUID();
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        // When
        quizService.deleteQuiz(quizId);

        // Then
        verify(quizRepository).delete(quiz);
    }

    @Test
    void givenNonExistentQuizId_whenDeleteQuiz_thenThrowsQuizNotFoundException() {
        // Given
        UUID quizId = UUID.randomUUID();
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(QuizNotFoundException.class, () -> quizService.deleteQuiz(quizId));
    }
}
