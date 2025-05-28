package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID courseId;
    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        courseDTO = new CourseDTO();
        courseDTO.setName("Intro to Java");
        courseDTO.setDescription("Java basics course");
        courseDTO.setInstructorId(UUID.randomUUID());
        courseDTO.setStudentIds(List.of(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    @DisplayName("POST /api/courses - should create course")
    void createCourse_ReturnsCreatedCourse() throws Exception {
        // Given
        Mockito.when(courseService.createCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        // When & Then
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Intro to Java"))
                .andExpect(jsonPath("$.description").value("Java basics course"));
    }

    @Test
    @DisplayName("GET /api/courses/{id} - should return course by ID")
    void getCourseById_ReturnsCourse() throws Exception {
        // Given
        Mockito.when(courseService.getCourseById(courseId)).thenReturn(courseDTO);

        // When & Then
        mockMvc.perform(get("/api/courses/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Intro to Java"))
                .andExpect(jsonPath("$.description").value("Java basics course"));
    }

    @Test
    @DisplayName("GET /api/courses - should return all courses")
    void getAllCourses_ReturnsListOfCourses() throws Exception {
        // Given
        CourseDTO secondCourse = new CourseDTO();
        secondCourse.setName("Spring Boot");
        secondCourse.setDescription("Spring Boot course");
        secondCourse.setInstructorId(UUID.randomUUID());
        secondCourse.setStudentIds(List.of(UUID.randomUUID()));

        Mockito.when(courseService.getAllCourses()).thenReturn(List.of(courseDTO, secondCourse));

        // When & Then
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[1].name").value("Spring Boot"));
    }

    @Test
    @DisplayName("PUT /api/courses/{id} - should update course")
    void updateCourse_ReturnsUpdatedCourse() throws Exception {
        // Given
        CourseDTO updatedCourse = new CourseDTO();
        updatedCourse.setName("Advanced Java");
        updatedCourse.setDescription("Updated Java content");
        updatedCourse.setInstructorId(UUID.randomUUID());
        updatedCourse.setStudentIds(List.of(UUID.randomUUID()));

        Mockito.when(courseService.updateCourse(eq(courseId), any(CourseDTO.class))).thenReturn(updatedCourse);

        // When & Then
        mockMvc.perform(put("/api/courses/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Advanced Java"))
                .andExpect(jsonPath("$.description").value("Updated Java content"));
    }

    @Test
    @DisplayName("DELETE /api/courses/{id} - should delete course")
    void deleteCourse_ReturnsNoContent() throws Exception {
        // Given
        Mockito.doNothing().when(courseService).deleteCourse(courseId);

        // When & Then
        mockMvc.perform(delete("/api/courses/{courseId}", courseId))
                .andExpect(status().isNoContent());
    }
}
