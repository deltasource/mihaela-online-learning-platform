package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.QuestionDTO;
import eu.deltasource.elearning.exception.QuestionNotFoundException;
import eu.deltasource.elearning.exception.QuizNotFoundException;
import eu.deltasource.elearning.model.Option;
import eu.deltasource.elearning.model.Question;
import eu.deltasource.elearning.model.Quiz;
import eu.deltasource.elearning.repository.QuestionRepository;
import eu.deltasource.elearning.repository.QuizRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Quiz quiz = quizRepository.findById(questionDTO.getQuizId())
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));

        Question question = new Question();
        question.setId(UUID.randomUUID());
        question.setQuiz(quiz);
        question.setQuestion(questionDTO.getQuestion());
        question.setQuestionType(questionDTO.getQuestionType());
        question.setPoints(questionDTO.getPoints());

        question = questionRepository.save(question);
        return mapToQuestionDTO(question);
    }

    public QuestionDTO getQuestionById(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found"));
        return mapToQuestionDTO(question);
    }

    public List<QuestionDTO> getQuestionsByQuizId(UUID quizId) {
        List<Question> questions = questionRepository.findByQuizId(quizId);
        return questions.stream()
                .map(this::mapToQuestionDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuestionDTO updateQuestion(UUID questionId, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found"));

        if (!existingQuestion.getQuiz().getId().equals(questionDTO.getQuizId())) {
            Quiz newQuiz = quizRepository.findById(questionDTO.getQuizId())
                    .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));
            existingQuestion.setQuiz(newQuiz);
        }

        existingQuestion.setQuestion(questionDTO.getQuestion());
        existingQuestion.setQuestionType(questionDTO.getQuestionType());
        existingQuestion.setPoints(questionDTO.getPoints());

        existingQuestion = questionRepository.save(existingQuestion);
        return mapToQuestionDTO(existingQuestion);
    }

    @Transactional
    public void deleteQuestion(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found"));
        questionRepository.delete(question);
    }

    @NotNull
    private QuestionDTO mapToQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setQuizId(question.getQuiz().getId());
        questionDTO.setQuestion(question.getQuestion());
        questionDTO.setQuestionType(question.getQuestionType());
        questionDTO.setPoints(question.getPoints());

        if (question.getOptions() != null) {
            questionDTO.setOptionIds(question.getOptions().stream()
                    .map(Option::getId)
                    .collect(Collectors.toList()));
        }

        return questionDTO;
    }
}
