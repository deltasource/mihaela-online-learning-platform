package eu.deltasource.elearning.exception;

/**
 * Exception thrown when a student cannot be found in the system.
 * This exception is typically thrown when attempting to retrieve, update, or delete a student
 * that does not exist in the database.
 */
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String email) {
        super("Student not found with this email: " + email);
    }
}
