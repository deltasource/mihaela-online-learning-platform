package eu.deltasource.demo.repository;

import eu.deltasource.demo.model.Student;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for managing Student entities.
 * This class provides in-memory storage and retrieval operations for Student objects.
 */
@Getter
@Component
public class StudentRepository {

    private final Map<String, Student> studentDatabase = new HashMap<>();

    public Optional<Student> getByEmail(String email) {
        return Optional.ofNullable(studentDatabase.get(email));
    }

    public void save(Student student) {
        studentDatabase.put(student.getEmail(), student);
    }

    public boolean remove(String email) {
        return studentDatabase.remove(email) != null;
    }
}
