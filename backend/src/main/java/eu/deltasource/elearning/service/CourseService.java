package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.InstructorRepository;
import eu.deltasource.elearning.repository.StudentRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Instructor instructor = instructorRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
        List<Student> students = studentRepository.findAllById(courseDTO.getStudentIds());
        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setInstructor(instructor);
        course.setStudents(students);
        course = courseRepository.save(course);
        return mapToCourseDTO(course);
    }

    public CourseDTO getCourseById(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        return mapToCourseDTO(course);
    }

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

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

    @Transactional
    public void deleteCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    public void enrollInCourse(UUID courseId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        student.getCourses().add(course);
        studentRepository.save(student);
    }

    @NotNull
    private CourseDTO mapToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName(course.getName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setInstructorId(course.getInstructor().getId());
        courseDTO.setStudentIds(course.getStudents().stream().map(Student::getId).collect(Collectors.toList()));
        return courseDTO;
    }

    @NotNull
    private Course mapToCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        Instructor instructor = instructorRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found"));
        course.setInstructor(instructor);
        List<Student> students = studentRepository.findAllById(courseDTO.getStudentIds());
        course.setStudents(students);
        return course;
    }
}
