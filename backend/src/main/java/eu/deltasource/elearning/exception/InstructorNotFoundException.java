package eu.deltasource.elearning.exception;

/**
 * Exception thrown when an instructor cannot be found in the system.
 * This exception is typically thrown when attempting to retrieve, update, or delete an instructor
 * that does not exist in the database.
 */
public class InstructorNotFoundException extends RuntimeException {
    /**
     * Constructs a new InstructorNotFoundException with a detailed message.
     *
     * @param email the email address of the instructor that was not found
     */
    public InstructorNotFoundException(String email) {
        super("Instructor with " + email + " not found.");
    }
}
