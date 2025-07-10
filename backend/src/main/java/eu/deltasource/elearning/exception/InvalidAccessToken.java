package eu.deltasource.elearning.exception;

public class InvalidAccessToken extends RuntimeException {
    public InvalidAccessToken(String message) {
        super(message);
    }
}
