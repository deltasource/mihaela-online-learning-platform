package eu.deltasource.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object (DTO) representing an error response.
 * This class is used to encapsulate error information to be sent back to the client.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {

    /**
     * The HTTP status code associated with the error.
     */
    private int status;

    /**
     * A descriptive message providing details about the error.
     */
    private String message;

    /**
     * The timestamp when the error occurred, represented as milliseconds since the epoch.
     */
    private long timestamp;
}
