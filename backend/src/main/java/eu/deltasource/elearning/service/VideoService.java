package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.VideoDTO;
import eu.deltasource.elearning.config.VideoFileManager;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.VideoNotFoundException;
import eu.deltasource.elearning.exception.VideoOperationException;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final CourseRepository courseRepository;
    private final VideoFileManager videoConfig;

    public VideoService(VideoRepository videoRepository, CourseRepository courseRepository, VideoFileManager videoConfig) {
        this.videoRepository = videoRepository;
        this.courseRepository = courseRepository;
        this.videoConfig = videoConfig;
    }

    @Transactional
    public Video uploadVideo(UUID courseId, MultipartFile file) {
        try {
            videoConfig.validateVideoFile(file);
            var course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CourseNotFoundException(courseId));
            String originalFilename = file.getOriginalFilename();
            String filePath = videoConfig.generateFilePath(originalFilename);
            videoConfig.saveVideoFile(file, filePath);
            Video video = new Video();
            video.setCourse(course);
            video.setFileName(originalFilename != null ? originalFilename : "video.mp4");
            video.setFilePath(filePath);

            return videoRepository.save(video);
        } catch (Exception e) {
            log.error("Error uploading video: {}", e.getMessage());
            throw new VideoOperationException("Failed to upload video", e);
        }
    }

    public Video getVideoById(UUID videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException(videoId));
    }

    public List<Video> getVideosByCourseId(UUID courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }
        return videoRepository.findByCourseId(courseId);
    }

    public VideoDTO mapToVideoDTO(Video video) {
        VideoDTO dto = new VideoDTO();
        dto.setFileName(video.getFileName());
        dto.setFilePath(video.getFilePath());
        dto.setCourseId(video.getCourse().getId());
        return dto;
    }

    public VideoDTO mapToVideoResponseDTO(Video video) {
        VideoDTO dto = new VideoDTO();
        dto.setCourseId(video.getId());
        dto.setFileName(video.getFileName());
        dto.setFilePath(video.getFilePath());
        dto.setCourseId(video.getCourse().getId());
        return dto;
    }

    public List<VideoDTO> getVideosByCourseIdAsDTO(UUID courseId) {
        return getVideosByCourseId(courseId).stream()
                .map(this::mapToVideoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VideoDTO updateVideo(UUID videoId, VideoDTO videoDTO) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException(videoId));

        if (videoDTO.getCourseId() != null) {
            var course = courseRepository.findById(videoDTO.getCourseId())
                    .orElseThrow(() -> new CourseNotFoundException(videoDTO.getCourseId()));
            video.setCourse(course);
        }

        video = videoRepository.save(video);
        return mapToVideoDTO(video);
    }

    @Transactional
    public void deleteVideo(UUID videoId) {
        try {
            Video video = videoRepository.findById(videoId)
                    .orElseThrow(() -> new VideoNotFoundException(videoId));
            videoConfig.deleteVideoFile(video.getFilePath());
            videoRepository.delete(video);
        } catch (Exception e) {
            log.error("Error deleting video: {}", e.getMessage());
            throw new VideoOperationException("Failed to delete video", e);
        }
    }
}
