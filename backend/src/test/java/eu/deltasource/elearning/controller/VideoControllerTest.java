package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.VideoDTO;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.service.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoControllerTest {

    @Mock
    private VideoService videoService;

    @InjectMocks
    private VideoController videoController;

    @Test
    void givenValidCourseIdAndFile_whenUploadVideo_thenReturnVideoDTO() {
        // Given
        UUID courseId = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);
        Video video = new Video();
        VideoDTO videoDTO = new VideoDTO();
        when(videoService.uploadVideo(courseId, file)).thenReturn(video);
        when(videoService.mapToVideoDTO(video)).thenReturn(videoDTO);

        // When
        VideoDTO response = videoController.uploadVideo(courseId, file);

        // Then
        assertEquals(videoDTO, response);
        verify(videoService, times(1)).uploadVideo(courseId, file);
        verify(videoService, times(1)).mapToVideoDTO(video);
    }

    @Test
    void givenValidCourseId_whenGetVideosByCourse_thenReturnListOfVideoDTOs() {
        // Given
        UUID courseId = UUID.randomUUID();
        List<VideoDTO> videoDTOs = List.of(new VideoDTO(), new VideoDTO());
        when(videoService.getVideosByCourseIdAsDTO(courseId)).thenReturn(videoDTOs);

        // When
        List<VideoDTO> response = videoController.getVideosByCourse(courseId);

        // Then
        assertEquals(videoDTOs, response);
        verify(videoService, times(1)).getVideosByCourseIdAsDTO(courseId);
    }

    @Test
    void givenValidVideoIdAndVideoDTO_whenUpdateVideo_thenReturnUpdatedVideoDTO() {
        // Given
        UUID videoId = UUID.randomUUID();
        VideoDTO videoDTO = new VideoDTO();
        when(videoService.updateVideo(videoId, videoDTO)).thenReturn(videoDTO);

        // When
        VideoDTO response = videoController.updateVideo(videoId, videoDTO);

        // Then
        assertEquals(videoDTO, response);
        verify(videoService, times(1)).updateVideo(videoId, videoDTO);
    }

    @Test
    void givenValidVideoId_whenDeleteVideo_thenVerifyServiceCalled() {
        // Given
        UUID videoId = UUID.randomUUID();
        doNothing().when(videoService).deleteVideo(videoId);

        // When
        videoController.deleteVideo(videoId);

        // Then
        verify(videoService, times(1)).deleteVideo(videoId);
    }
}
