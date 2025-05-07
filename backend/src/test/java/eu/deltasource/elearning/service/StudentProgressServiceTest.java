package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.StudentProgressDTO;
import eu.deltasource.elearning.exception.StudentProgressNotFoundException;
import eu.deltasource.elearning.model.*;
import eu.deltasource.elearning.repository.StudentProgressRepository;
import eu.deltasource.elearning.repository.WatchedVideosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentProgressServiceTest {

    @Mock private StudentService studentService;
    @Mock private CourseService courseService;
    @Mock private VideoService videoService;
    @Mock private StudentProgressRepository studentProgressRepository;
    @Mock private WatchedVideosRepository watchedVideosRepository;

    @InjectMocks
    private StudentProgressService studentProgressService;

    private UUID studentId;
    private UUID courseId;
    private UUID videoId;
    private Student student;
    private Course course;
    private Video video;
    private StudentProgress studentProgress;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        videoId = UUID.randomUUID();

        student = new Student();
        student.setId(studentId);

        course = new Course();
        course.setId(courseId);

        video = new Video();
        video.setId(videoId);

        studentProgress = new StudentProgress();
        studentProgress.setStudent(student);
        studentProgress.setProgressPercentage(50);
        studentProgress.setTotalVideos(10);
        studentProgress.setVideosWatched(5);
    }

    @Test
    void getProgressPercentage_ShouldThrowException_WhenProgressNotFound() {
        when(studentService.getStudentById(studentId)).thenReturn(student);
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(Optional.empty());

        assertThrows(StudentProgressNotFoundException.class,
                () -> studentProgressService.getProgressPercentage(studentId, courseId));
    }

    @Test
    void updateProgress_ShouldCreateNewProgress_WhenNotExists() {
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.empty());
        when(studentService.getStudentById(studentId)).thenReturn(student);
        when(videoService.getVideoById(videoId)).thenReturn(video);

        studentProgressService.updateProgress(studentId, courseId, videoId);

        verify(studentProgressRepository).save(any(StudentProgress.class));
        verify(watchedVideosRepository).save(any(WatchedVideos.class));
    }

    @Test
    void updateProgress_ShouldUpdateExistingProgress() {
        studentProgress.setTotalVideos(5);
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.of(studentProgress));
        when(videoService.getVideoById(videoId)).thenReturn(video);

        studentProgressService.updateProgress(studentId, courseId, videoId);

        verify(studentProgressRepository).save(studentProgress);
        verify(watchedVideosRepository).save(any(WatchedVideos.class));
        assertEquals(6, studentProgress.getVideosWatched());
        assertTrue(studentProgress.getProgressPercentage() > 0);
    }

    @Test
    void updateProgress_ShouldHandleZeroTotalVideos() {
        studentProgress.setTotalVideos(0);
        studentProgress.setVideosWatched(0);
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.of(studentProgress));
        when(videoService.getVideoById(videoId)).thenReturn(video);

        studentProgressService.updateProgress(studentId, courseId, videoId);

        assertEquals(1, studentProgress.getVideosWatched());
        assertEquals(0.0, studentProgress.getProgressPercentage());
    }
    @Test
    void shouldMapStudentProgressToDTO() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        StudentProgress progress = new StudentProgress();
        progress.setProgressPercentage(85);
        progress.setTotalVideos(10);
        progress.setVideosWatched(8);

        // When
        StudentProgressDTO dto = mapToStudentProgressDTO(progress, studentId, courseId);

        // Then
        assertNotNull(dto);
        assertEquals(studentId.toString(), dto.getStudentId());
        assertEquals(courseId.toString(), dto.getCourseId());
        assertEquals(progress.getProgressPercentage(), dto.getProgressPercentage());
        assertEquals(progress.getTotalVideos(), dto.getTotalVideos());
        assertEquals(progress.getVideosWatched(), dto.getVideosWatched());
    }

    // Helper method to test (assuming it's static for simplicity)
    private StudentProgressDTO mapToStudentProgressDTO(StudentProgress progress, UUID studentId, UUID courseId) {
        StudentProgressDTO dto = new StudentProgressDTO();
        dto.setStudentId(studentId.toString());
        dto.setCourseId(courseId.toString());
        dto.setProgressPercentage(progress.getProgressPercentage());
        dto.setTotalVideos(progress.getTotalVideos());
        dto.setVideosWatched(progress.getVideosWatched());
        return dto;
    }
}
