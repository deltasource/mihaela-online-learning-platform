package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.VideoDTO;
import eu.deltasource.elearning.exception.InvalidVideoFormatException;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Video Management", description = "APIs for managing course videos")
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private static final long BYTES_IN_GB = 1024L * 1024L * 1024L;
    private static final long MAX_FILE_SIZE = 10L * BYTES_IN_GB;
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
    public VideoDTO uploadVideo(@PathVariable UUID courseId, @RequestParam("file") MultipartFile file) throws IOException {
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new InvalidVideoFormatException("File size exceeds the maximum allowed size of 10 GB");
            }
            Video uploadedVideo = videoService.uploadVideo(courseId, file);
            return videoService.mapToVideoDTO(uploadedVideo);
    }

    @Operation(summary = "Get videos by course", description = "Retrieves all videos associated with a specific course")
    @GetMapping("/courses/{courseId}")
    public List<VideoDTO> getVideosByCourse(@PathVariable UUID courseId) {
        return videoService.getVideosByCourseIdAsDTO(courseId);
    }

    @Operation(summary = "Update a video", description = "Updates an existing video's metadata")
    @PutMapping("/{videoId}")
    public VideoDTO updateVideo(@PathVariable UUID videoId, @RequestBody VideoDTO videoDTO) {
        return videoService.updateVideo(videoId, videoDTO);
    }

    @Operation(summary = "Delete a video", description = "Deletes a video by its ID")
    @DeleteMapping("/{videoId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteVideo(@PathVariable UUID videoId) {
        videoService.deleteVideo(videoId);
    }
}
