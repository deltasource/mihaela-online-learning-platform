package eu.deltasource.elearning.globalExceptionHandler;

import eu.deltasource.elearning.DTOs.ErrorResponse;
import eu.deltasource.elearning.controller.globalExceptionHandler.ELearningGlobalExceptionHandler;
import eu.deltasource.elearning.exception.NotFoundException;
import eu.deltasource.elearning.exception.StudentAlreadyExistsException;
import eu.deltasource.elearning.exception.StudentNotFoundException;
import eu.deltasource.elearning.exception.UnsupportedFileTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ELearningGlobalExceptionHandlerTest {

    private final ELearningGlobalExceptionHandler handler = new ELearningGlobalExceptionHandler();

    @Test
    void givenStudentAlreadyExistsException_whenHandleStudentAlreadyExistsException_thenReturnsConflict() {
        // Given
        StudentAlreadyExistsException ex = new StudentAlreadyExistsException("Student exists");
        WebRequest request = mock(WebRequest.class);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleStudentAlreadyExistsException(ex, request);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void givenRuntimeException_whenHandleException_thenReturnsBadRequest() {
        // Given
        RuntimeException ex = new RuntimeException("Runtime error");
        WebRequest request = mock(WebRequest.class);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleException(ex, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void givenStudentNotFoundException_whenHandleException_thenReturnsNotFound() {
        // Given
        StudentNotFoundException ex = new StudentNotFoundException("Student not found");
        WebRequest request = mock(WebRequest.class);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleException(ex, request);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void givenNotFoundException_whenHandleNotFoundException_thenReturnsNotFound() {
        // Given
        NotFoundException ex = new NotFoundException("Not found");
        WebRequest request = mock(WebRequest.class);

        // When
        ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(ex, request);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void givenUnsupportedFileTypeException_whenHandleUnsupportedFileType_thenReturnsBadRequest() {
        // Given
        UnsupportedFileTypeException ex = new UnsupportedFileTypeException("Bad file");

        // When
        ResponseEntity<ErrorResponse> response = handler.handleUnsupportedFileType(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
    }
}
