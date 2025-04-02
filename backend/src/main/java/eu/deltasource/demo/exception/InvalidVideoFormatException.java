package eu.deltasource.demo.exception;

public class InvalidVideoFormatException extends RuntimeException {
    public InvalidVideoFormatException(String message) {
        super(message);
    }
}
