package eu.deltasource.elearning.exception;

import java.util.UUID;

public class VideoNotFoundException extends RuntimeException {

    public VideoNotFoundException(String message) {
        super(message);
    }

    public VideoNotFoundException(UUID videoId) {
        super("Video not found with ID: " + videoId);
    }
}
