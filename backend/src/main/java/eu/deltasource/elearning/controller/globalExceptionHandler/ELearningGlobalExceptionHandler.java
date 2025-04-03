package eu.deltasource.elearning.controller.globalExceptionHandler;

import eu.deltasource.elearning.DTOs.ErrorResponse;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.exception.StudentNotFoundException;
import eu.deltasource.elearning.exception.VideoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Global exception handler for the E-Learning application.
 * Provides centralized exception handling across all @RequestMapping methods
 * through @ExceptionHandler methods.
 */
@ControllerAdvice
public class ELearningGlobalExceptionHandler {

    /**
     * Handles StudentNotFoundException and returns a NOT_FOUND error response.
     *
     * @param e The StudentNotFoundException that was thrown
     * @param request The current request
     * @return A ResponseEntity with NOT_FOUND status and error details
     */
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(StudentNotFoundException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles InstructorNotFoundException and returns a NOT_FOUND error response.
     *
     * @param e The InstructorNotFoundException that was thrown
     * @param request The current request
     * @return A ResponseEntity with NOT_FOUND status and error details
     */
    @ExceptionHandler(InstructorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInstructorNotFoundException(InstructorNotFoundException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles CourseNotFoundException and returns a NOT_FOUND error response.
     *
     * @param e The CourseNotFoundException that was thrown
     * @param request The current request
     * @return A ResponseEntity with NOT_FOUND status and error details
     */
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCourseNotFoundException(CourseNotFoundException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles VideoNotFoundException and returns a NOT_FOUND error response.
     *
     * @param e The VideoNotFoundException that was thrown
     * @param request The current request
     * @return A ResponseEntity with NOT_FOUND status and error details
     */
    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVideoNotFoundException(VideoNotFoundException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    /**
     * Handles general RuntimeExceptions and returns a BAD_REQUEST error response.
     * Acts as a catch-all for RuntimeExceptions not handled by more specific handlers.
     *
     * @param e The RuntimeException that was thrown
     * @param request The current request
     * @return A ResponseEntity with BAD_REQUEST status and error details
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
