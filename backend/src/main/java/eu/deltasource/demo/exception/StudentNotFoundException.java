package eu.deltasource.demo.exception;

/**
 * Exception thrown when a student cannot be found in the system.
 * This exception is typically thrown when attempting to retrieve, update, or delete a student
 * that does not exist in the database.
 */
public class StudentNotFoundException extends RuntimeException {
    /**
     * Constructs a new StudentNotFoundException with a detailed message.
     *
     * @param email the email address of the student that was not found
     */
    public StudentNotFoundException(String email) {
        super("Student not found with this email: " + email);
    }
}
