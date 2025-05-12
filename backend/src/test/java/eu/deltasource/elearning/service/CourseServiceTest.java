package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Instructor;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.InstructorRepository;
import eu.deltasource.elearning.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    private UUID courseId;
    private UUID instructorId;
    private List<UUID> studentIds;
    private Course course;
    private Instructor instructor;
    private List<Student> students;
    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        instructorId = UUID.randomUUID();
        studentIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        instructor = new Instructor();
        instructor.setId(instructorId);
        instructor.setEmail("instructor@example.com");
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDepartment("Computer Science");

        students = new ArrayList<>();
        for (int i = 0; i < studentIds.size(); i++) {
            Student student = new Student();
            student.setId(studentIds.get(i));
            student.setEmail("student" + i + "@example.com");
            student.setFirstName("Student");
            student.setLastName("" + i);
            students.add(student);
        }

        course = new Course();
        course.setId(courseId);
        course.setName("Java Programming");
        course.setDescription("Learn Java from scratch");
        course.setInstructor(instructor);
        course.setStudents(students);
        course.setVideos(new ArrayList<>());

        courseDTO = new CourseDTO();
        courseDTO.setName("Java Programming");
        courseDTO.setDescription("Learn Java from scratch");
        courseDTO.setInstructorId(instructorId);
        courseDTO.setStudentIds(studentIds);
    }

    @Test
    void createCourse_ShouldReturnCourseDTO() {
        // Given
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(studentRepository.findAllById(studentIds)).thenReturn(students);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        CourseDTO result = courseService.createCourse(courseDTO);

        // Then
        assertNotNull(result);
        assertEquals(courseDTO.getName(), result.getName());
        assertEquals(courseDTO.getDescription(), result.getDescription());
        assertEquals(courseDTO.getInstructorId(), result.getInstructorId());
        assertEquals(courseDTO.getStudentIds().size(), result.getStudentIds().size());

        verify(instructorRepository).findById(instructorId);
        verify(studentRepository).findAllById(studentIds);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void createCourse_WithInvalidInstructor_ShouldThrowException() {
        // Given
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> courseService.createCourse(courseDTO));
        verify(instructorRepository).findById(instructorId);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void getCourseById_ShouldReturnCourseDTO() {
        // Given
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // When
        CourseDTO result = courseService.getCourseById(courseId);

        // Then
        assertNotNull(result);
        assertEquals(course.getName(), result.getName());
        assertEquals(course.getDescription(), result.getDescription());
        assertEquals(course.getInstructor().getId(), result.getInstructorId());
        assertEquals(course.getStudents().stream().map(Student::getId).collect(Collectors.toList()),
                result.getStudentIds());

        verify(courseRepository).findById(courseId);
    }

    @Test
    void getCourseById_WithInvalidId_ShouldThrowException() {
        // Given
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> courseService.getCourseById(courseId));
        verify(courseRepository).findById(courseId);
    }

    @Test
    void getAllCourses_ShouldReturnListOfCourseDTOs() {
        // Given
        List<Course> courses = List.of(course);
        when(courseRepository.findAll()).thenReturn(courses);

        // When
        List<CourseDTO> result = courseService.getAllCourses();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(course.getName(), result.get(0).getName());
        assertEquals(course.getDescription(), result.get(0).getDescription());

        verify(courseRepository).findAll();
    }

    @Test
    void updateCourse_ShouldReturnUpdatedCourseDTO() {
        // Given
        CourseDTO updatedDTO = new CourseDTO();
        updatedDTO.setName("Updated Java Course");
        updatedDTO.setDescription("Updated description");
        updatedDTO.setInstructorId(instructorId);
        updatedDTO.setStudentIds(studentIds);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(studentRepository.findAllById(studentIds)).thenReturn(students);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        CourseDTO result = courseService.updateCourse(courseId, updatedDTO);

        // Then
        assertNotNull(result);
        assertEquals(updatedDTO.getName(), result.getName());
        assertEquals(updatedDTO.getDescription(), result.getDescription());

        verify(courseRepository).findById(courseId);
        verify(instructorRepository).findById(instructorId);
        verify(studentRepository).findAllById(studentIds);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void updateCourse_WithInvalidCourseId_ShouldThrowException() {
        // Given
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> courseService.updateCourse(courseId, courseDTO));
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void updateCourse_WithInvalidInstructorId_ShouldThrowException() {
        // Given
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> courseService.updateCourse(courseId, courseDTO));
        verify(courseRepository).findById(courseId);
        verify(instructorRepository).findById(instructorId);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void deleteCourse_ShouldDeleteCourse() {
        // Given
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        // When
        courseService.deleteCourse(courseId);

        // Then
        verify(courseRepository).findById(courseId);
        verify(courseRepository).delete(course);
    }

    @Test
    void deleteCourse_WithInvalidId_ShouldThrowException() {
        // Given
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> courseService.deleteCourse(courseId));
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).delete(any(Course.class));
    }

    @Test
    void createCourse_WithEmptyStudentList_ShouldSucceed() {
        // Given
        courseDTO.setStudentIds(new ArrayList<>());
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(studentRepository.findAllById(new ArrayList<>())).thenReturn(new ArrayList<>());
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        CourseDTO result = courseService.createCourse(courseDTO);

        // Then
        assertNotNull(result);
        assertEquals(courseDTO.getName(), result.getName());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void updateCourse_WithEmptyStudentList_ShouldSucceed() {
        // Given
        CourseDTO updatedDTO = new CourseDTO();
        updatedDTO.setName("Updated Course");
        updatedDTO.setDescription("Updated Description");
        updatedDTO.setInstructorId(instructorId);
        updatedDTO.setStudentIds(new ArrayList<>());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(studentRepository.findAllById(new ArrayList<>())).thenReturn(new ArrayList<>());
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // When
        CourseDTO result = courseService.updateCourse(courseId, updatedDTO);

        // Then
        assertNotNull(result);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void createCourse_WithNullName_ShouldThrowException() {
        // Given
        courseDTO.setName(null);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(studentRepository.findAllById(studentIds)).thenReturn(students);

        // When & Then
        assertThrows(NullPointerException.class, () -> courseService.createCourse(courseDTO));
    }

    @Test
    void updateCourse_WithNullDescription_ShouldThrowException() {
        // Given
        courseDTO.setDescription(null);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(studentRepository.findAllById(studentIds)).thenReturn(students);

        // When & Then
        assertThrows(NullPointerException.class, () -> courseService.updateCourse(courseId, courseDTO));
    }
}
