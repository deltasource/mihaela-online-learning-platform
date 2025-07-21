package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.QuizDTO;
import eu.deltasource.elearning.service.QuizService;
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

@Tag(name = "Quiz Management")
@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a new quiz")
    public QuizDTO createQuiz(@RequestBody @Valid QuizDTO quizDTO) {
        log.info("Creating a new quiz: {}", quizDTO);
        return quizService.createQuiz(quizDTO);
    }

    @GetMapping("/{quizId}")
    @Operation(summary = "Get quiz by ID")
    public QuizDTO getQuizById(@PathVariable @NotNull UUID quizId) {
        log.info("Retrieving quiz with ID: {}", quizId);
        return quizService.getQuizById(quizId);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get all quizzes for a course")
    public List<QuizDTO> getQuizzesByCourseId(@PathVariable @NotNull UUID courseId) {
        log.info("Retrieving all quizzes for course ID: {}", courseId);
        return quizService.getQuizzesByCourseId(courseId);
    }

    @PutMapping("/{quizId}")
    @Operation(summary = "Update a quiz")
    public QuizDTO updateQuiz(
            @PathVariable @NotNull UUID quizId,
            @RequestBody @Valid QuizDTO quizDTO) {
        log.info("Updating quiz with ID: {}", quizId);
        return quizService.updateQuiz(quizId, quizDTO);
    }

    @DeleteMapping("/{quizId}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a quiz")
    public void deleteQuiz(@PathVariable @NotNull UUID quizId) {
        log.info("Deleting quiz with ID: {}", quizId);
        quizService.deleteQuiz(quizId);
    }
}
