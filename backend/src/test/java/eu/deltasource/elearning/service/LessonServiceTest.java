package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.LessonDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.LessonNotFoundException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Lesson;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void givenValidLessonDTO_whenCreateLesson_thenSavesAndReturnsDTO() {
        // Given
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setCourseId(courseId);
        lessonDTO.setTitle("Lesson 1");
        lessonDTO.setDescription("Desc");
        lessonDTO.setContent("Content");
        lessonDTO.setVideoUrl("url");
        lessonDTO.setDuration(10);
        lessonDTO.setOrder(1);
        lessonDTO.setPublished(true);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(inv -> {
            Lesson l = inv.getArgument(0);
            l.setId(UUID.randomUUID());
            return l;
        });

        // When
        LessonDTO result = lessonService.createLesson(lessonDTO);

        // Then
        assertEquals("Lesson 1", result.getTitle());
        assertEquals(courseId, result.getCourseId());
        assertTrue(result.isPublished());
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void givenNonExistentCourse_whenCreateLesson_thenThrowsCourseNotFoundException() {
        // Given
        UUID courseId = UUID.randomUUID();
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setCourseId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> lessonService.createLesson(lessonDTO));
    }

    @Test
    void givenExistingLessonId_whenGetLessonById_thenReturnsDTO() {
        // Given
        UUID lessonId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setCourse(course);
        lesson.setTitle("Lesson");
        lesson.setDescription("Desc");
        lesson.setContent("Content");
        lesson.setVideoUrl("url");
        lesson.setDuration(10);
        lesson.setOrder(2);
        lesson.setPublished(false);
        lesson.setCreatedAt(LocalDateTime.now());
        lesson.setUpdatedAt(LocalDateTime.now());
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        // When
        LessonDTO dto = lessonService.getLessonById(lessonId);

        // Then
        assertEquals(lessonId, dto.getId());
        assertEquals(courseId, dto.getCourseId());
        assertEquals("Lesson", dto.getTitle());
    }

    @Test
    void givenNonExistentLessonId_whenGetLessonById_thenThrowsLessonNotFoundException() {
        // Given
        UUID lessonId = UUID.randomUUID();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(LessonNotFoundException.class, () -> lessonService.getLessonById(lessonId));
    }

    @Test
    void givenCourseId_whenGetLessonsByCourseId_thenReturnsList() {
        // Given
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        Lesson l1 = new Lesson();
        l1.setId(UUID.randomUUID());
        l1.setCourse(course);
        l1.setOrder(1);
        l1.setTitle("L1");
        l1.setCreatedAt(LocalDateTime.now());
        l1.setUpdatedAt(LocalDateTime.now());
        Lesson l2 = new Lesson();
        l2.setId(UUID.randomUUID());
        l2.setCourse(course);
        l2.setOrder(2);
        l2.setTitle("L2");
        l2.setCreatedAt(LocalDateTime.now());
        l2.setUpdatedAt(LocalDateTime.now());
        when(lessonRepository.findByCourseIdOrderByOrderAsc(courseId)).thenReturn(Arrays.asList(l1, l2));

        // When
        List<LessonDTO> result = lessonService.getLessonsByCourseId(courseId);

        // Then
        assertEquals(2, result.size());
        assertEquals("L1", result.get(0).getTitle());
        assertEquals("L2", result.get(1).getTitle());
    }

    @Test
    void givenValidUpdate_whenUpdateLesson_thenUpdatesAndReturnsDTO() {
        // Given
        UUID lessonId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        Lesson existing = new Lesson();
        existing.setId(lessonId);
        existing.setCourse(course);
        existing.setTitle("Old");
        existing.setDescription("OldDesc");
        existing.setContent("OldContent");
        existing.setVideoUrl("oldUrl");
        existing.setDuration(5);
        existing.setOrder(1);
        existing.setPublished(false);
        existing.setCreatedAt(LocalDateTime.now());
        existing.setUpdatedAt(LocalDateTime.now());
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setCourseId(courseId);
        lessonDTO.setTitle("New");
        lessonDTO.setDescription("NewDesc");
        lessonDTO.setContent("NewContent");
        lessonDTO.setVideoUrl("newUrl");
        lessonDTO.setDuration(15);
        lessonDTO.setOrder(2);
        lessonDTO.setPublished(true);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(existing));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        LessonDTO result = lessonService.updateLesson(lessonId, lessonDTO);

        // Then
        assertEquals("New", result.getTitle());
        assertEquals("NewDesc", result.getDescription());
        assertEquals("NewContent", result.getContent());
        assertEquals("newUrl", result.getVideoUrl());
        assertEquals(15, result.getDuration());
        assertTrue(result.isPublished());
        verify(lessonRepository).save(existing);
    }

    @Test
    void givenUpdateWithCourseChange_whenUpdateLesson_thenUpdatesCourse() {
        // Given
        UUID lessonId = UUID.randomUUID();
        UUID oldCourseId = UUID.randomUUID();
        UUID newCourseId = UUID.randomUUID();
        Course oldCourse = new Course();
        oldCourse.setId(oldCourseId);
        Course newCourse = new Course();
        newCourse.setId(newCourseId);
        Lesson existing = new Lesson();
        existing.setId(lessonId);
        existing.setCourse(oldCourse);
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setCourseId(newCourseId);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(existing));
        when(courseRepository.findById(newCourseId)).thenReturn(Optional.of(newCourse));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        LessonDTO result = lessonService.updateLesson(lessonId, lessonDTO);

        // Then
        assertEquals(newCourseId, result.getCourseId());
        verify(lessonRepository).save(existing);
    }

    @Test
    void givenUpdateWithNonExistentLesson_whenUpdateLesson_thenThrowsLessonNotFoundException() {
        // Given
        UUID lessonId = UUID.randomUUID();
        LessonDTO lessonDTO = new LessonDTO();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(LessonNotFoundException.class, () -> lessonService.updateLesson(lessonId, lessonDTO));
    }

    @Test
    void givenUpdateWithNonExistentCourse_whenUpdateLesson_thenThrowsCourseNotFoundException() {
        // Given
        UUID lessonId = UUID.randomUUID();
        UUID oldCourseId = UUID.randomUUID();
        UUID newCourseId = UUID.randomUUID();
        Course oldCourse = new Course();
        oldCourse.setId(oldCourseId);
        Lesson existing = new Lesson();
        existing.setId(lessonId);
        existing.setCourse(oldCourse);
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setCourseId(newCourseId);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(existing));
        when(courseRepository.findById(newCourseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> lessonService.updateLesson(lessonId, lessonDTO));
    }

    @Test
    void givenExistingLessonId_whenDeleteLesson_thenDeletesLesson() {
        // Given
        UUID lessonId = UUID.randomUUID();
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        // When
        lessonService.deleteLesson(lessonId);

        // Then
        verify(lessonRepository).delete(lesson);
    }

    @Test
    void givenNonExistentLessonId_whenDeleteLesson_thenThrowsLessonNotFoundException() {
        // Given
        UUID lessonId = UUID.randomUUID();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(LessonNotFoundException.class, () -> lessonService.deleteLesson(lessonId));
    }
}
