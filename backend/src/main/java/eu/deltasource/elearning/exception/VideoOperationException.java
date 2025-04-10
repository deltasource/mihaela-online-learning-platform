package eu.deltasource.elearning.exception;

public class VideoOperationException extends RuntimeException {

    public VideoOperationException(String message) {
        super(message);
    }

    public VideoOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
