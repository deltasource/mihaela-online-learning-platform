package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.StudentProgressDTO;
import eu.deltasource.elearning.exception.StudentProgressNotFoundException;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.model.StudentProgress;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.repository.StudentProgressRepository;
import eu.deltasource.elearning.repository.WatchedVideosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentProgressServiceTest {

    @Mock
    private StudentService studentService;
    @Mock
    private CourseService courseService;
    @Mock
    private VideoService videoService;
    @Mock
    private StudentProgressRepository studentProgressRepository;
    @Mock
    private WatchedVideosRepository watchedVideosRepository;

    @InjectMocks
    private StudentProgressService studentProgressService;

    @Test
    void givenValidIds_whenGetProgressPercentage_thenReturnsDTO() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        StudentProgress progress = new StudentProgress();
        progress.setProgressPercentage(50.0);
        progress.setTotalVideos(4);
        progress.setVideosWatched(2);
        when(studentService.getStudentById(studentId)).thenReturn(new Student());
        when(courseService.getCourseById(courseId)).thenReturn(null);
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.of(progress));

        // When
        StudentProgressDTO actualDto = studentProgressService.getProgressPercentage(studentId, courseId);

        // Then
        assertEquals(50.0, actualDto.getProgressPercentage());
        assertEquals(4, actualDto.getTotalVideos());
        assertEquals(2, actualDto.getVideosWatched());
        assertEquals(studentId.toString(), actualDto.getStudentId());
        assertEquals(courseId.toString(), actualDto.getCourseId());
    }

    @Test
    void givenMissingProgress_whenGetProgressPercentage_thenThrowsException() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        when(studentService.getStudentById(studentId)).thenReturn(new Student());
        when(courseService.getCourseById(courseId)).thenReturn(null);
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(StudentProgressNotFoundException.class, () ->
                studentProgressService.getProgressPercentage(studentId, courseId));
    }

    @Test
    void givenExistingProgress_whenUpdateProgress_thenMarksVideoAndUpdatesProgress() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();
        StudentProgress progress = new StudentProgress();
        progress.setVideosWatched(1);
        progress.setTotalVideos(4);
        Video video = new Video();
        when(videoService.getVideoById(videoId)).thenReturn(video);
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.of(progress));
        when(studentProgressRepository.save(any(StudentProgress.class))).thenReturn(progress);

        // When
        studentProgressService.updateProgress(studentId, courseId, videoId);

        // Then
        verify(watchedVideosRepository).save(any());
        verify(studentProgressRepository).save(progress);
        assertEquals(2, progress.getVideosWatched());
        assertEquals(50.0, progress.getProgressPercentage());
    }

    @Test
    void givenNoExistingProgress_whenUpdateProgress_thenCreatesProgressAndMarksVideo() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();
        Student student = new Student();
        Video video = new Video();
        when(studentService.getStudentById(studentId)).thenReturn(student);
        when(videoService.getVideoById(videoId)).thenReturn(video);
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.empty());
        when(studentProgressRepository.save(any(StudentProgress.class))).thenReturn(new StudentProgress());

        // When
        studentProgressService.updateProgress(studentId, courseId, videoId);

        // Then
        verify(watchedVideosRepository).save(any());
        verify(studentProgressRepository, atLeastOnce()).save(any());
    }
}
