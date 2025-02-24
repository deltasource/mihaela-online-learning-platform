package eu.deltasource.demo.repository;

import eu.deltasource.demo.model.Instructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for managing Instructor entities.
 * This class provides in-memory storage and retrieval operations for Instructor objects.
 */
@Getter
@Component
public class InstructorRepository {

    private final Map<String, Instructor> instructorDatabase = new HashMap<>();

    public Optional<Instructor> getByEmail(String email) {
        return Optional.ofNullable(instructorDatabase.get(email));
    }

    public void save(Instructor instructor) {
        instructorDatabase.put(instructor.getEmail(), instructor);
    }

    public boolean remove(String email) {
        return instructorDatabase.remove(email) != null;
    }
}
