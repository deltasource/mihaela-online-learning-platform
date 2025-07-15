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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private VideoFileManager videoFileManager;
    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private VideoService videoService;

    @Test
    void givenValidCourseAndFile_whenUploadVideo_thenSavesAndReturnsVideo() {
        // Given
        UUID courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(multipartFile.getOriginalFilename()).thenReturn("video.mp4");
        when(videoFileManager.generateFilePath("video.mp4")).thenReturn("/videos/video.mp4");
        doNothing().when(videoFileManager).validateVideoFile(multipartFile);
        doNothing().when(videoFileManager).saveVideoFile(multipartFile, "/videos/video.mp4");
        Video savedVideo = new Video();
        savedVideo.setCourse(course);
        savedVideo.setFileName("video.mp4");
        savedVideo.setFilePath("/videos/video.mp4");
        when(videoRepository.save(any(Video.class))).thenReturn(savedVideo);

        // When
        Video result = videoService.uploadVideo(courseId, multipartFile);

        // Then
        assertEquals("video.mp4", result.getFileName());
        assertEquals("/videos/video.mp4", result.getFilePath());
        assertEquals(course, result.getCourse());
        verify(videoRepository).save(any(Video.class));
    }

    @Test
    void givenExceptionDuringUpload_whenUploadVideo_thenThrowsVideoOperationException() {
        // Given
        UUID courseId = UUID.randomUUID();
        doThrow(new RuntimeException("fail")).when(videoFileManager).validateVideoFile(multipartFile);

        // When & Then
        assertThrows(VideoOperationException.class, () -> videoService.uploadVideo(courseId, multipartFile));
    }

    @Test
    void givenExistingVideoId_whenGetVideoById_thenReturnsVideo() {
        // Given
        UUID videoId = UUID.randomUUID();
        Video video = new Video();
        video.setId(videoId);
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));

        // When
        Video result = videoService.getVideoById(videoId);

        // Then
        assertEquals(videoId, result.getId());
    }

    @Test
    void givenNonExistentVideoId_whenGetVideoById_thenThrowsVideoNotFoundException() {
        // Given
        UUID videoId = UUID.randomUUID();
        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(VideoNotFoundException.class, () -> videoService.getVideoById(videoId));
    }

    @Test
    void givenExistingCourseId_whenGetVideosByCourseId_thenReturnsList() {
        // Given
        UUID courseId = UUID.randomUUID();
        when(courseRepository.existsById(courseId)).thenReturn(true);
        List<Video> videos = Arrays.asList(new Video(), new Video());
        when(videoRepository.findByCourseId(courseId)).thenReturn(videos);

        // When
        List<Video> result = videoService.getVideosByCourseId(courseId);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void givenNonExistentCourseId_whenGetVideosByCourseId_thenThrowsCourseNotFoundException() {
        // Given
        UUID courseId = UUID.randomUUID();
        when(courseRepository.existsById(courseId)).thenReturn(false);

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> videoService.getVideosByCourseId(courseId));
    }

    @Test
    void givenVideo_whenMapToVideoDTO_thenReturnsDTO() {
        // Given
        Video video = new Video();
        video.setFileName("file.mp4");
        video.setFilePath("/path/file.mp4");
        Course course = new Course();
        UUID courseId = UUID.randomUUID();
        course.setId(courseId);
        video.setCourse(course);

        // When
        VideoDTO dto = videoService.mapToVideoDTO(video);

        // Then
        assertEquals("file.mp4", dto.getFileName());
        assertEquals("/path/file.mp4", dto.getFilePath());
        assertEquals(courseId, dto.getCourseId());
    }

    @Test
    void givenVideo_whenMapToVideoResponseDTO_thenReturnsDTO() {
        // Given
        Video video = new Video();
        video.setFileName("file.mp4");
        video.setFilePath("/path/file.mp4");
        UUID videoId = UUID.randomUUID();
        video.setId(videoId);
        Course course = new Course();
        UUID courseId = UUID.randomUUID();
        course.setId(courseId);
        video.setCourse(course);

        // When
        VideoDTO dto = videoService.mapToVideoResponseDTO(video);

        // Then
        assertEquals("file.mp4", dto.getFileName());
        assertEquals("/path/file.mp4", dto.getFilePath());
        assertEquals(courseId, dto.getCourseId());
    }

    @Test
    void givenCourseId_whenGetVideosByCourseIdAsDTO_thenReturnsDTOList() {
        // Given
        UUID courseId = UUID.randomUUID();
        when(courseRepository.existsById(courseId)).thenReturn(true);
        Video video = new Video();
        Course course = new Course();
        course.setId(courseId);
        video.setCourse(course);
        video.setFileName("file.mp4");
        video.setFilePath("/path/file.mp4");
        when(videoRepository.findByCourseId(courseId)).thenReturn(List.of(video));

        // When
        List<VideoDTO> dtos = videoService.getVideosByCourseIdAsDTO(courseId);

        // Then
        assertEquals(1, dtos.size());
        assertEquals("file.mp4", dtos.get(0).getFileName());
    }

    @Test
    void givenExistingVideoIdAndDTO_whenUpdateVideo_thenUpdatesAndReturnsDTO() {
        // Given
        UUID videoId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        Video video = new Video();
        video.setId(videoId);
        Course oldCourse = new Course();
        oldCourse.setId(UUID.randomUUID());
        video.setCourse(oldCourse);
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setCourseId(courseId);
        Course newCourse = new Course();
        newCourse.setId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(newCourse));
        when(videoRepository.save(any(Video.class))).thenReturn(video);

        // When
        VideoDTO result = videoService.updateVideo(videoId, videoDTO);

        // Then
        assertEquals(courseId, result.getCourseId());
        verify(videoRepository).save(video);
    }

    @Test
    void givenNonExistentVideoId_whenUpdateVideo_thenThrowsVideoNotFoundException() {
        // Given
        UUID videoId = UUID.randomUUID();
        VideoDTO videoDTO = new VideoDTO();
        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(VideoNotFoundException.class, () -> videoService.updateVideo(videoId, videoDTO));
    }

    @Test
    void givenNonExistentCourseIdInDTO_whenUpdateVideo_thenThrowsCourseNotFoundException() {
        // Given
        UUID videoId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        Video video = new Video();
        video.setId(videoId);
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setCourseId(courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseNotFoundException.class, () -> videoService.updateVideo(videoId, videoDTO));
    }

    @Test
    void givenExistingVideoId_whenDeleteVideo_thenDeletesVideoAndFile() {
        // Given
        UUID videoId = UUID.randomUUID();
        Video video = new Video();
        video.setId(videoId);
        video.setFilePath("/path/file.mp4");
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        doNothing().when(videoFileManager).deleteVideoFile("/path/file.mp4");
        doNothing().when(videoRepository).delete(video);

        // When
        videoService.deleteVideo(videoId);

        // Then
        verify(videoFileManager).deleteVideoFile("/path/file.mp4");
        verify(videoRepository).delete(video);
    }

    @Test
    void givenExceptionDuringDelete_whenDeleteVideo_thenThrowsVideoOperationException() {
        // Given
        UUID videoId = UUID.randomUUID();
        Video video = new Video();
        video.setId(videoId);
        video.setFilePath("/path/file.mp4");
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        doThrow(new RuntimeException("fail")).when(videoFileManager).deleteVideoFile("/path/file.mp4");

        // When & Then
        assertThrows(VideoOperationException.class, () -> videoService.deleteVideo(videoId));
    }
}
