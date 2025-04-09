package eu.deltasource.elearning.controller;

import eu.deltasource.elearning.DTOs.VideoDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.InvalidVideoFormatException;
import eu.deltasource.elearning.exception.VideoNotFoundException;
import eu.deltasource.elearning.exception.VideoOperationException;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public VideoDTO uploadVideo(@PathVariable UUID courseId, @RequestParam("file") MultipartFile file) {
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

    @ExceptionHandler({
            InvalidVideoFormatException.class,
            VideoOperationException.class,
            VideoNotFoundException.class,
            CourseNotFoundException.class
    })
    public ResponseEntity<Map<String, String>> handleExceptions(Exception e) {
        Map<String, String> response = new HashMap<>();
        HttpStatus status;

        if (e instanceof InvalidVideoFormatException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e instanceof VideoNotFoundException || e instanceof CourseNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        response.put("error", e.getMessage());
        return new ResponseEntity<>(response, status);
    }
}
