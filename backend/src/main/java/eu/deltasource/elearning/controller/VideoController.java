package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.VideoDTO;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Video Management", description = "APIs for managing course videos")
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Operation(
            summary = "Upload a video for a course",
            description = "Uploads a video file and associates it with a specific course"
    )
    @PostMapping(value = "/{courseId}/upload", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public VideoDTO uploadVideo(@PathVariable @NotNull UUID courseId, @RequestParam("file") @NotNull MultipartFile file) {
        Video uploadedVideo = videoService.uploadVideo(courseId, file);
        return videoService.mapToVideoDTO(uploadedVideo);
    }

    @Operation(summary = "Get videos by course", description = "Retrieves all videos associated with a specific course")
    @GetMapping("/courses/{courseId}")
    public List<VideoDTO> getVideosByCourse(@PathVariable @NotNull UUID courseId) {
        return videoService.getVideosByCourseIdAsDTO(courseId);
    }

    @Operation(summary = "Update a video", description = "Updates an existing video's metadata")
    @PutMapping("/{videoId}")
    public VideoDTO updateVideo(@PathVariable @NotNull UUID videoId, @RequestBody @NotNull @Valid VideoDTO videoDTO) {
        return videoService.updateVideo(videoId, videoDTO);
    }

    @Operation(summary = "Delete a video", description = "Deletes a video by its ID")
    @DeleteMapping("/{videoId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteVideo(@PathVariable @NotNull UUID videoId) {
        videoService.deleteVideo(videoId);
    }
}
