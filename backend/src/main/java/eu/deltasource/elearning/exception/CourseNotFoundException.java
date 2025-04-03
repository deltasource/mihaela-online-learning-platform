package eu.deltasource.elearning.exception;

import java.util.UUID;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super(message);
    }

    public CourseNotFoundException(UUID courseId) {
        super("Course not found with ID: " + courseId);
    }
}
