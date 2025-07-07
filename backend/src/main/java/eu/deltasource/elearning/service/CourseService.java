package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.model.Lesson;
import eu.deltasource.elearning.model.Quiz;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.InstructorRepository;
import eu.deltasource.elearning.repository.LessonRepository;
import eu.deltasource.elearning.repository.QuizRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final LessonRepository lessonRepository;
    private final QuizRepository quizRepository;

    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Instructor instructor = instructorRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found"));

        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setThumbnail(courseDTO.getThumbnail());
        course.setCategory(courseDTO.getCategory());
        course.setLevel(courseDTO.getLevel());
        course.setDuration(courseDTO.getDuration());
        course.setRating(courseDTO.getRating());
        course.setRatingCount(courseDTO.getRatingCount());
        course.setEnrollmentCount(courseDTO.getEnrollmentCount());
        course.setPrice(courseDTO.getPrice());
        course.setPublished(courseDTO.isPublished());
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        course.setInstructor(instructor);

        course = courseRepository.save(course);
        return mapToCourseDTO(course);
    }

    public CourseDTO getCourseById(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        return mapToCourseDTO(course);
    }

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    @Transactional
    public CourseDTO updateCourse(UUID courseId, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        Instructor instructor = instructorRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found"));

        existingCourse.setName(courseDTO.getName());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setThumbnail(courseDTO.getThumbnail());
        existingCourse.setCategory(courseDTO.getCategory());
        existingCourse.setLevel(courseDTO.getLevel());
        existingCourse.setDuration(courseDTO.getDuration());
        existingCourse.setPrice(courseDTO.getPrice());
        existingCourse.setPublished(courseDTO.isPublished());
        existingCourse.setUpdatedAt(LocalDateTime.now());
        existingCourse.setInstructor(instructor);

        existingCourse = courseRepository.save(existingCourse);
        return mapToCourseDTO(existingCourse);
    }

    @Transactional
    public void deleteCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    @NotNull
    private CourseDTO mapToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setThumbnail(course.getThumbnail());
        courseDTO.setCategory(course.getCategory());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setDuration(course.getDuration());
        courseDTO.setRating(course.getRating());
        courseDTO.setRatingCount(course.getRatingCount());
        courseDTO.setEnrollmentCount(course.getEnrollmentCount());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setPublished(course.isPublished());
        courseDTO.setCreatedAt(course.getCreatedAt());
        courseDTO.setUpdatedAt(course.getUpdatedAt());
        courseDTO.setInstructorId(course.getInstructor().getId());

        if (course.getLessons() != null) {
            courseDTO.setLessonIds(course.getLessons().stream()
                    .map(lesson -> lesson.getId().toString())
                    .collect(Collectors.toList()));
        }

        if (course.getQuizzes() != null) {
            courseDTO.setQuizIds(course.getQuizzes().stream()
                    .map(quiz -> quiz.getId().toString())
                    .collect(Collectors.toList()));
        }

        return courseDTO;
    }
}
