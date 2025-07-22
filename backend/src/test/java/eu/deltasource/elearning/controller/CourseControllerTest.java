package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidCourseDTO_whenCreateCourse_thenReturnsCreatedCourse() throws Exception {
        // Given
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("Java Basics");
        courseDTO.setDescription("Learn Java from scratch");
        when(courseService.createCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        // When & Then
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isCreated());
        verify(courseService, times(1)).createCourse(any(CourseDTO.class));
    }

    @Test
    void givenCourseId_whenGetCourseById_thenReturnsCourse() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(courseId);
        courseDTO.setName("Java Basics");
        when(courseService.getCourseById(courseId)).thenReturn(courseDTO);

        // When & Then
        mockMvc.perform(get("/api/courses/{courseId}", courseId))
                .andExpect(status().isOk());
        verify(courseService, times(1)).getCourseById(courseId);
    }

    @Test
    void whenGetAllCourses_thenReturnsListOfCourses() throws Exception {
        // Given
        List<CourseDTO> courses = List.of(new CourseDTO(), new CourseDTO());
        when(courseService.getAllCourses()).thenReturn(courses);

        // When & Then
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk());
        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    void givenCourseIdAndDTO_whenUpdateCourse_thenReturnsUpdatedCourse() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName("Advanced Java");
        courseDTO.setDescription("Deep dive into Java");
        when(courseService.updateCourse(eq(courseId), any(CourseDTO.class))).thenReturn(courseDTO);

        // When & Then
        mockMvc.perform(put("/api/courses/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk());
        verify(courseService, times(1)).updateCourse(eq(courseId), any(CourseDTO.class));
    }

    @Test
    void givenCourseId_whenDeleteCourse_thenReturnsNoContent() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        doNothing().when(courseService).deleteCourse(courseId);

        // When & Then
        mockMvc.perform(delete("/api/courses/{courseId}", courseId))
                .andExpect(status().isNoContent());
        verify(courseService, times(1)).deleteCourse(courseId);
    }
}
