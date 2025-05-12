package eu.deltasource.elearning.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.deltasource.elearning.DTOs.VideoDTO;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideoController.class)
public class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VideoService videoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void uploadVideo_given_then() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "video.mp4", "video/mp4", "dummy content".getBytes());
        Video dummyVideo = new Video();
        dummyVideo.setFileName("video.mp4");
        dummyVideo.setFilePath("/videos/video.mp4");
        Course course = new Course();
        course.setId(courseId);
        dummyVideo.setCourse(course);
        VideoDTO expectedDTO = new VideoDTO();
        expectedDTO.setFileName("video.mp4");
        expectedDTO.setFilePath("/videos/video.mp4");
        expectedDTO.setCourseId(courseId);

        // When
        when(videoService.uploadVideo(courseId, file)).thenReturn(dummyVideo);
        when(videoService.mapToVideoDTO(dummyVideo)).thenReturn(expectedDTO);

        // Then
        mockMvc.perform(multipart("/api/videos/{courseId}/upload", courseId)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fileName").value("video.mp4"))
                .andExpect(jsonPath("$.filePath").value("/videos/video.mp4"))
                .andExpect(jsonPath("$.courseId").value(courseId.toString()));
    }

    @Test
    public void getVideosByCourse_given_then() throws Exception {
        // Given
        UUID courseId = UUID.randomUUID();
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setFileName("video.mp4");
        videoDTO.setFilePath("/videos/video.mp4");
        videoDTO.setCourseId(courseId);
        List<VideoDTO> list = List.of(videoDTO);

        // When
        when(videoService.getVideosByCourseId(courseId)).thenReturn(List.of(new Video()));
        when(videoService.mapToVideoDTO(any(Video.class))).thenReturn(videoDTO);
        when(videoService.getVideosByCourseIdAsDTO(courseId)).thenReturn(list);

        // Then
        mockMvc.perform(get("/api/videos/courses/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileName").value("video.mp4"))
                .andExpect(jsonPath("$[0].filePath").value("/videos/video.mp4"))
                .andExpect(jsonPath("$[0].courseId").value(courseId.toString()));
    }

    @Test
    public void updateVideo_given_then() throws Exception {
        // Given
        UUID videoId = UUID.randomUUID();
        VideoDTO updateDTO = new VideoDTO();
        updateDTO.setFileName("updated.mp4");
        updateDTO.setFilePath("/videos/updated.mp4");
        UUID courseId = UUID.randomUUID();
        updateDTO.setCourseId(courseId);

        // When
        when(videoService.getVideoById(videoId)).thenReturn(new Video());
        when(videoService.mapToVideoDTO(any(Video.class))).thenReturn(updateDTO);
        when(videoService.updateVideo(videoId, updateDTO)).thenReturn(updateDTO);

        // Then
        mockMvc.perform(put("/api/videos/{videoId}", videoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("updated.mp4"))
                .andExpect(jsonPath("$.filePath").value("/videos/updated.mp4"))
                .andExpect(jsonPath("$.courseId").value(courseId.toString()));
    }

    @Test
    public void deleteVideo_given_then() throws Exception {
        // Given
        UUID videoId = UUID.randomUUID();

        // When
        mockMvc.perform(delete("/api/videos/{videoId}", videoId))
                .andExpect(status().isNoContent());

        // Then
        verify(videoService, times(1)).deleteVideo(videoId);
    }
}
