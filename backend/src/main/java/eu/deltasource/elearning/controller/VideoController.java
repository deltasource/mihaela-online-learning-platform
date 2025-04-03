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

@RestController
@RequestMapping("/api/videos")
@Tag(name = "Video Management", description = "APIs for managing course videos")
public class VideoController {

    private final VideoService videoService;

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024 * 1024;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Operation(
            summary = "Upload a video for a course",
            description = "Uploads a video file and associates it with a specific course"
    )
    @PostMapping("/{courseId}/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public VideoDTO uploadVideo(
            @PathVariable UUID courseId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidVideoFormatException("File size exceeds the maximum allowed size of 10 GB");
        }

        Video uploadedVideo = videoService.uploadVideo(courseId, file);
        return videoService.mapToVideoDTO(uploadedVideo);
    }

    @Operation(
            summary = "Get videos by course",
            description = "Retrieves all videos associated with a specific course"
    )
    @GetMapping("/courses/{courseId}")
    public List<VideoDTO> getVideosByCourse(
            @PathVariable UUID courseId
    ) {
        return videoService.getVideosByCourseIdAsDTO(courseId);
    }

    @Operation(
            summary = "Update a video",
            description = "Updates an existing video's metadata"
    )
    @PutMapping("/{videoId}")
    public VideoDTO updateVideo(
            @PathVariable UUID videoId,
            @RequestBody VideoDTO videoDTO
    ) {
        return videoService.updateVideo(videoId, videoDTO);
    }

    @Operation(
            summary = "Delete a video",
            description = "Deletes a video by its ID"
    )
    @DeleteMapping("/{videoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideo(
            @PathVariable UUID videoId
    ) {
        videoService.deleteVideo(videoId);
    }
}
