package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.VideoDTO;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The @AutoConfigureMockMvc(addFilters = false) annotation is used in Spring Boot testing to disable the automatic addition of Spring Security filters when configuring MockMvc.
 * By default, when @AutoConfigureMockMvc is used, any registered filters (such as security filters) are applied to the MockMvc instance. Setting addFilters = false prevents these filters from being added, allowing tests to bypass security constraints and focus on controller logic without authentication or authorization requirements.
 **/
@WebMvcTest(VideoController.class)
@AutoConfigureMockMvc(addFilters = false)
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VideoService videoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidCourseIdAndFile_whenUploadVideo_thenReturnsCreatedAndServiceCalled() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "dummy".getBytes());
        Video video = new Video();
        VideoDTO videoDTO = new VideoDTO();
        when(videoService.uploadVideo(eq(courseId), any())).thenReturn(video);
        when(videoService.mapToVideoDTO(video)).thenReturn(videoDTO);

        // When
        var result = mockMvc.perform(multipart("/api/videos/{courseId}/upload", courseId)
                .file(file));

        // Then
        result.andExpect(status().isCreated());
        verify(videoService, times(1)).uploadVideo(eq(courseId), any());
        verify(videoService, times(1)).mapToVideoDTO(video);
    }

    @Test
    void givenValidCourseId_whenGetVideosByCourse_thenReturnsOkAndServiceCalled() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        List<VideoDTO> videoDTOs = List.of(new VideoDTO(), new VideoDTO());
        when(videoService.getVideosByCourseIdAsDTO(courseId)).thenReturn(videoDTOs);

        // When
        var result = mockMvc.perform(get("/api/videos/courses/{courseId}", courseId));

        // Then
        result.andExpect(status().isOk());
        verify(videoService, times(1)).getVideosByCourseIdAsDTO(courseId);
    }

    @Test
    void givenValidVideoId_whenDeleteVideo_thenReturnsNoContentAndServiceCalled() throws Exception {
        // Given
        UUID videoId = UUID.randomUUID();
        doNothing().when(videoService).deleteVideo(videoId);

        // When
        var result = mockMvc.perform(delete("/api/videos/{videoId}", videoId));

        // Then
        result.andExpect(status().isNoContent());
        verify(videoService, times(1)).deleteVideo(eq(videoId));
    }

    @Test
    void givenInvalidUUID_whenUploadVideo_thenReturnsBadRequest() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "dummy".getBytes());

        // When
        var result = mockMvc.perform(multipart("/api/videos/{courseId}/upload", "invalid-uuid")
                .file(file));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidUUID_whenGetVideosByCourse_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(get("/api/videos/courses/{courseId}", "invalid-uuid"));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidUUID_whenUpdateVideo_thenReturnsBadRequest() throws Exception {
        // Given
        VideoDTO videoDTO = new VideoDTO();

        // When
        var result = mockMvc.perform(put("/api/videos/{videoId}", "invalid-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(videoDTO)));

        // Then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void givenInvalidUUID_whenDeleteVideo_thenReturnsBadRequest() throws Exception {
        // When
        var result = mockMvc.perform(delete("/api/videos/{videoId}", "invalid-uuid"));

        // Then
        result.andExpect(status().isBadRequest());
    }
}
