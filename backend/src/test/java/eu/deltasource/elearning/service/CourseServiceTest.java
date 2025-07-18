package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.InstructorRepository;
import eu.deltasource.elearning.repository.LessonRepository;
import eu.deltasource.elearning.repository.QuizRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void givenValidCourseDTO_whenCreateCourse_thenReturnsCourseDTO() {
        // Given
        UUID courseId = UUID.randomUUID();
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        Course course = new Course();
        course.setId(courseId);
        course.setName("Test Course");
        course.setInstructor(instructor);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(courseId);
        courseDTO.setName("Test Course");
        courseDTO.setInstructorId(instructorId);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        CourseDTO result = courseService.createCourse(courseDTO);

        // Then
        assertEquals(courseId, result.getId());
        assertEquals("Test Course", result.getName());
    }

    @Test
    void givenNonExistentInstructor_whenCreateCourse_thenThrowsException() {
        // Given
        UUID instructorId = UUID.randomUUID();
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setInstructorId(instructorId);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InstructorNotFoundException.class, () -> courseService.createCourse(courseDTO));
    }

    @Test
    void givenNonExistentCourseId_whenGetCourseById_thenThrowsException() {
        // Given
        UUID courseId = UUID.randomUUID();
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(courseId));
    }

    @Test
    void givenValidCourseDTO_whenUpdateCourse_thenReturnsUpdatedCourseDTO() {
        // Given
        UUID courseId = UUID.randomUUID();
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        Course course = new Course();
        course.setId(courseId);
        course.setName("Test Course");
        course.setInstructor(instructor);
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(courseId);
        courseDTO.setName("Test Course");
        courseDTO.setInstructorId(instructorId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        CourseDTO result = courseService.updateCourse(courseId, courseDTO);

        // Then
        assertEquals(courseId, result.getId());
        assertEquals("Test Course", result.getName());
    }

    @Test
    void givenNonExistentCourseId_whenUpdateCourse_thenThrowsException() {
        // Given
        UUID courseId = UUID.randomUUID();
        CourseDTO courseDTO = new CourseDTO();
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(courseId, courseDTO));
    }

    @Test
    void givenNonExistentInstructor_whenUpdateCourse_thenThrowsException() {
        // Given
        UUID courseId = UUID.randomUUID();
        UUID instructorId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setInstructorId(instructorId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InstructorNotFoundException.class, () -> courseService.updateCourse(courseId, courseDTO));
    }

    @Test
    void givenExistingCourseId_whenDeleteCourse_thenDeletesCourse() {
        // Given
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        // When
        courseService.deleteCourse(courseId);

        // Then
        verify(courseRepository).delete(course);
    }

    @Test
    void givenNonExistentCourseId_whenDeleteCourse_thenThrowsException() {
        // Given
        UUID courseId = UUID.randomUUID();

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> courseService.deleteCourse(courseId));
    }
}
