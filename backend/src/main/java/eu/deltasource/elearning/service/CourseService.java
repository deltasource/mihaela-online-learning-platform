package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.InstructorRepository;
import eu.deltasource.elearning.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    /**
     * Creates a new course and saves it to the repository.
     *
     * @param courseDTO The DTO containing the course details.
     * @return The created CourseDTO.
     */
    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Instructor instructor = instructorRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        List<Student> students = studentRepository.findAllById(courseDTO.getStudentIds());

        Course course = new Course();
        course.setId(UUID.randomUUID());
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setInstructor(instructor);
        course.setStudents(students);

        course = courseRepository.save(course);
        return mapToCourseDTO(course);
    }

    /**
     * Retrieves a course by its ID.
     *
     * @param courseId The ID of the course to retrieve.
     * @return The CourseDTO with course details.
     */
    public CourseDTO getCourseById(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        return mapToCourseDTO(course);
    }

    /**
     * Retrieves all courses.
     *
     * @return A list of all courses.
     */
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    /**
     * Updates a course by its ID.
     *
     * @param courseId  The ID of the course to update.
     * @param courseDTO The updated course details.
     * @return The updated CourseDTO.
     */
    @Transactional
    public CourseDTO updateCourse(UUID courseId, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Instructor instructor = instructorRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));

        List<Student> students = studentRepository.findAllById(courseDTO.getStudentIds());

        existingCourse.setName(courseDTO.getName());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setInstructor(instructor);
        existingCourse.setStudents(students);

        existingCourse = courseRepository.save(existingCourse);
        return mapToCourseDTO(existingCourse);
    }

    /**
     * Deletes a course by its ID.
     *
     * @param courseId The ID of the course to delete.
     */
    @Transactional
    public void deleteCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    /**
     * Maps a Course entity to a CourseDTO.
     *
     * @param course The entity to map.
     * @return The mapped CourseDTO.
     */
    private CourseDTO mapToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName(course.getName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setInstructorId(course.getInstructor().getId());
        courseDTO.setStudentIds(course.getStudents().stream().map(Student::getId).collect(Collectors.toList()));
        return courseDTO;
    }
}
