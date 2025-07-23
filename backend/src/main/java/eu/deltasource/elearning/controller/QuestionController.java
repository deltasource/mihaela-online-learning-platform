package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.QuestionDTO;
import eu.deltasource.elearning.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Question Management")
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a new question")
    public QuestionDTO createQuestion(@RequestBody @Valid QuestionDTO questionDTO) {
        log.info("Creating question: {}", questionDTO);
        return questionService.createQuestion(questionDTO);
    }

    @GetMapping("/{questionId}")
    @Operation(summary = "Get question by ID")
    public QuestionDTO getQuestionById(@PathVariable @NotNull UUID questionId) {
        log.info("Retrieving question with ID: {}", questionId);
        return questionService.getQuestionById(questionId);
    }

    @GetMapping("/quiz/{quizId}")
    @Operation(summary = "Get all questions for a quiz")
    public List<QuestionDTO> getQuestionsByQuizId(@PathVariable @NotNull UUID quizId) {
        log.info("Retrieving all questions for quiz ID: {}", quizId);
        return questionService.getQuestionsByQuizId(quizId);
    }

    @PutMapping("/{questionId}")
    @Operation(summary = "Update a question")
    public QuestionDTO updateQuestion(
            @PathVariable @NotNull UUID questionId,
            @RequestBody @Valid QuestionDTO questionDTO) {
        log.info("Updating question with ID: {}", questionId);
        return questionService.updateQuestion(questionId, questionDTO);
    }

    @DeleteMapping("/{questionId}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a question")
    public void deleteQuestion(@PathVariable @NotNull UUID questionId) {
        log.info("Deleting question with ID: {}", questionId);
        questionService.deleteQuestion(questionId);
    }
}
