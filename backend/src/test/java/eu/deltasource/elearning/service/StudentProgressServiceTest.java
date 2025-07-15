package eu.deltasource.elearning.service;

import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.model.StudentProgress;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.model.WatchedVideos;
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
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(Optional.of(progress));
        when(studentProgressRepository.save(any(StudentProgress.class))).thenReturn(progress);

        // When
        studentProgressService.updateProgress(studentId, courseId, videoId);

        // Then
        verify(watchedVideosRepository).save(any(WatchedVideos.class));
        verify(studentProgressRepository).save(progress);
        assertEquals(2, progress.getVideosWatched());
    }

    @Test
    void givenNoExistingProgress_whenUpdateProgress_thenCreatesProgressAndMarksVideo() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID videoId = UUID.randomUUID();
        Student student = new Student();
        when(studentService.getStudentById(studentId)).thenReturn(student);
        when(videoService.getVideoById(videoId)).thenReturn(new Video());
        when(studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(Optional.empty());
        when(studentProgressRepository.save(any(StudentProgress.class))).thenReturn(new StudentProgress());

        // When
        studentProgressService.updateProgress(studentId, courseId, videoId);

        // Then
        verify(watchedVideosRepository).save(any(WatchedVideos.class));
        verify(studentProgressRepository).save(any(StudentProgress.class));
    }
}
