package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.QuizDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.QuizNotFoundException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Question;
import eu.deltasource.elearning.model.Quiz;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.QuizRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public QuizDTO createQuiz(QuizDTO quizDTO) {
        Course course = courseRepository.findById(quizDTO.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        Quiz quiz = new Quiz();
        quiz.setId(UUID.randomUUID());
        quiz.setCourse(course);
        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setPassingScore(quizDTO.getPassingScore());
        quiz.setTimeLimit(quizDTO.getTimeLimit());
        quiz.setOrder(quizDTO.getOrder());
        quiz.setPublished(quizDTO.isPublished());
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());

        quiz = quizRepository.save(quiz);
        return mapToQuizDTO(quiz);
    }

    public QuizDTO getQuizById(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));
        return mapToQuizDTO(quiz);
    }

    public List<QuizDTO> getQuizzesByCourseId(UUID courseId) {
        List<Quiz> quizzes = quizRepository.findByCourseIdOrderByOrderAsc(courseId);
        return quizzes.stream()
                .map(this::mapToQuizDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuizDTO updateQuiz(UUID quizId, QuizDTO quizDTO) {
        Quiz existingQuiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));

        if (!existingQuiz.getCourse().getId().equals(quizDTO.getCourseId())) {
            Course newCourse = courseRepository.findById(quizDTO.getCourseId())
                    .orElseThrow(() -> new CourseNotFoundException("Course not found"));
            existingQuiz.setCourse(newCourse);
        }

        existingQuiz.setTitle(quizDTO.getTitle());
        existingQuiz.setDescription(quizDTO.getDescription());
        existingQuiz.setPassingScore(quizDTO.getPassingScore());
        existingQuiz.setTimeLimit(quizDTO.getTimeLimit());
        existingQuiz.setOrder(quizDTO.getOrder());
        existingQuiz.setPublished(quizDTO.isPublished());
        existingQuiz.setUpdatedAt(LocalDateTime.now());

        existingQuiz = quizRepository.save(existingQuiz);
        return mapToQuizDTO(existingQuiz);
    }

    @Transactional
    public void deleteQuiz(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));
        quizRepository.delete(quiz);
    }

    @NotNull
    private QuizDTO mapToQuizDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quiz.getId());
        quizDTO.setCourseId(quiz.getCourse().getId());
        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setDescription(quiz.getDescription());
        quizDTO.setPassingScore(quiz.getPassingScore());
        quizDTO.setTimeLimit(quiz.getTimeLimit());
        quizDTO.setOrder(quiz.getOrder());
        quizDTO.setPublished(quiz.isPublished());
        quizDTO.setCreatedAt(quiz.getCreatedAt());
        quizDTO.setUpdatedAt(quiz.getUpdatedAt());

        if (quiz.getQuestions() != null) {
            quizDTO.setQuestionIds(quiz.getQuestions().stream()
                    .map(Question::getId)
                    .collect(Collectors.toList()));
        }

        return quizDTO;
    }
}
