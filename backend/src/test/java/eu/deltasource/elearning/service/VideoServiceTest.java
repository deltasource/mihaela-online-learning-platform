package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.VideoDTO;
import eu.deltasource.elearning.config.VideoFileManager;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.VideoNotFoundException;
import eu.deltasource.elearning.exception.VideoOperationException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private VideoFileManager videoFileManager;

    @InjectMocks
    private VideoService videoService;

    private Course course;
    private Video video;
    private UUID courseId;
    private UUID videoId;

    @BeforeEach
    void setup() {
        courseId = UUID.randomUUID();
        videoId = UUID.randomUUID();

        course = new Course();
        course.setId(courseId);

        video = new Video();
        video.setId(videoId);
        video.setFileName("lecture.mp4");
        video.setFilePath("/videos/lecture.mp4");
        video.setCourse(course);
    }

    @Test
    void uploadVideo_ShouldReturnSavedVideo() throws Exception {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("lecture.mp4");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(videoFileManager).validateVideoFile(mockFile);
        doNothing().when(videoFileManager).saveVideoFile(any(), anyString());
        when(videoFileManager.generateFilePath("lecture.mp4")).thenReturn("/videos/lecture.mp4");
        when(videoRepository.save(any(Video.class))).thenReturn(video);

        // When
        Video result = videoService.uploadVideo(courseId, mockFile);

        // Then
        assertNotNull(result);
        assertEquals("lecture.mp4", result.getFileName());
        verify(videoRepository).save(any(Video.class));
    }

    @Test
    void uploadVideo_ShouldThrowException_WhenCourseNotFound() {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(VideoOperationException.class, () -> videoService.uploadVideo(courseId, mockFile));
    }

    @Test
    void getVideoById_ShouldReturnVideo() {
        // Given
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));

        // When
        Video result = videoService.getVideoById(videoId);

        // Then
        assertEquals(videoId, result.getId());
    }

    @Test
    void getVideoById_ShouldThrow_WhenNotFound() {
        // Given
        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(VideoNotFoundException.class, () -> videoService.getVideoById(videoId));
    }

    @Test
    void getVideosByCourseId_ShouldReturnVideos() {
        // Given
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(videoRepository.findByCourseId(courseId)).thenReturn(List.of(video));

        // When
        List<Video> result = videoService.getVideosByCourseId(courseId);

        // Then
        assertEquals(1, result.size());
        assertEquals("lecture.mp4", result.get(0).getFileName());
    }

    @Test
    void getVideosByCourseId_ShouldThrow_WhenCourseNotFound() {
        // Given
        when(courseRepository.existsById(courseId)).thenReturn(false);

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> videoService.getVideosByCourseId(courseId));
    }

    @Test
    void mapToVideoDTO_ShouldReturnCorrectDTO() {
        // When
        VideoDTO dto = videoService.mapToVideoDTO(video);

        // Then
        assertEquals("lecture.mp4", dto.getFileName());
        assertEquals("/videos/lecture.mp4", dto.getFilePath());
        assertEquals(courseId, dto.getCourseId());
    }

    @Test
    void mapToVideoResponseDTO_ShouldReturnCorrectDTO() {
        // When
        VideoDTO dto = videoService.mapToVideoResponseDTO(video);

        // Then
        assertEquals("lecture.mp4", dto.getFileName());
        assertEquals("/videos/lecture.mp4", dto.getFilePath());
        assertEquals(courseId, dto.getCourseId());
    }

    @Test
    void getVideosByCourseIdAsDTO_ShouldReturnListOfDTOs() {
        // Given
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(videoRepository.findByCourseId(courseId)).thenReturn(List.of(video));

        // When
        List<VideoDTO> result = videoService.getVideosByCourseIdAsDTO(courseId);

        // Then
        assertEquals(1, result.size());
        assertEquals("lecture.mp4", result.get(0).getFileName());
    }

    @Test
    void updateVideo_ShouldUpdateCourseAndReturnDTO() {
        // Given
        VideoDTO dto = new VideoDTO();
        dto.setCourseId(courseId);

        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(videoRepository.save(any())).thenReturn(video);

        // When
        VideoDTO result = videoService.updateVideo(videoId, dto);

        // Then
        assertEquals(courseId, result.getCourseId());
    }

    @Test
    void updateVideo_ShouldThrow_WhenVideoNotFound() {
        // Given
        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(VideoNotFoundException.class, () -> videoService.updateVideo(videoId, new VideoDTO()));
    }

    @Test
    void deleteVideo_ShouldRemoveVideo() {
        // Given
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        doNothing().when(videoFileManager).deleteVideoFile(anyString());
        doNothing().when(videoRepository).delete(video);

        // When
        assertDoesNotThrow(() -> videoService.deleteVideo(videoId));

        // Then
        verify(videoRepository).delete(video);
    }

    @Test
    void deleteVideo_ShouldThrow_WhenExceptionOccurs() {
        // Given
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        doThrow(new RuntimeException("I/O error")).when(videoFileManager).deleteVideoFile(anyString());

        // When & Then
        assertThrows(VideoOperationException.class, () -> videoService.deleteVideo(videoId));
    }
}
