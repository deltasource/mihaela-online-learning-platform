package eu.deltasource.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing an error response.
 * This class is used to encapsulate error information to be sent back to the client.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {

    private int status;

    private String message;

    private LocalDateTime timestamp;
}
