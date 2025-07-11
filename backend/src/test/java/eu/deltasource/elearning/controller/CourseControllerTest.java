package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void givenValidCourseData_whenCreateCourse_thenCourseIsSuccessfullyCreated() {
        // Given
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("Java Basics");
        courseDTO.setDescription("Learn Java from scratch");

        when(courseService.createCourse(courseDTO)).thenReturn(courseDTO);

        // When
        CourseDTO response = courseController.createCourse(courseDTO);

        // Then
        assertEquals(courseDTO, response);
        verify(courseService, times(1)).createCourse(courseDTO);
    }

    @Test
    void givenCourseIdExists_whenGetCourseById_thenCorrectCourseIsReturned() {
        // Given
        UUID courseId = UUID.randomUUID();
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(courseId);
        courseDTO.setName("Java Basics");

        when(courseService.getCourseById(courseId)).thenReturn(courseDTO);

        // When
        CourseDTO response = courseController.getCourseById(courseId);

        // Then
        assertEquals(courseDTO, response);
        verify(courseService, times(1)).getCourseById(courseId);
    }

    @Test
    void givenCoursesExist_whenGetAllCourses_thenAllCoursesAreReturned() {
        // Given
        List<CourseDTO> courses = List.of(new CourseDTO(), new CourseDTO());
        when(courseService.getAllCourses()).thenReturn(courses);

        // When
        List<CourseDTO> response = courseController.getAllCourses();

        // Then
        assertEquals(courses, response);
        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    void givenCourseExists_whenUpdateCourse_thenCourseIsUpdatedSuccessfully() {
        // Given
        UUID courseId = UUID.randomUUID();
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("Advanced Java");
        courseDTO.setDescription("Deep dive into Java");

        when(courseService.updateCourse(courseId, courseDTO)).thenReturn(courseDTO);

        // When
        CourseDTO response = courseController.updateCourse(courseId, courseDTO);

        // Then
        assertEquals(courseDTO, response);
        verify(courseService, times(1)).updateCourse(courseId, courseDTO);
    }

    @Test
    void givenCourseExists_whenDeleteCourse_thenCourseIsDeletedSuccessfully() {
        // Given
        UUID courseId = UUID.randomUUID();

        doNothing().when(courseService).deleteCourse(courseId);

        // When
        courseController.deleteCourse(courseId);

        // Then
        verify(courseService, times(1)).deleteCourse(courseId);
    }
}
