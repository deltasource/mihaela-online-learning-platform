package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.VideoDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.InvalidVideoFormatException;
import eu.deltasource.elearning.exception.VideoNotFoundException;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.repository.VideoRepository;
import eu.deltasource.elearning.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Value("${video.upload.directory}")
    private String videoUploadDirectory;

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024 * 1024;

    private final VideoRepository videoRepository;
    private final CourseRepository courseRepository;

    public VideoService(VideoRepository videoRepository, CourseRepository courseRepository) {
        this.videoRepository = videoRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Video uploadVideo(UUID courseId, MultipartFile file) throws IOException {
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidVideoFormatException("File size exceeds the maximum allowed size of 10 GB");
        }

        if (file.isEmpty()) {
            throw new InvalidVideoFormatException("File cannot be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            throw new InvalidVideoFormatException("Only video files are allowed");
        }

        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "-" + (originalFilename != null ? originalFilename : "video.mp4");
        String filePath = Paths.get(videoUploadDirectory, fileName).toString();

        File directory = new File(videoUploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File destinationFile = new File(filePath);
        Files.copy(file.getInputStream(), destinationFile.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        Video video = new Video();
        video.setCourse(course);
        video.setFileName(fileName);
        video.setFilePath(filePath);

        return videoRepository.save(video);
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
        dto.setId(video.getId());
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
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException(videoId));

        try {
            File file = new File(video.getFilePath());
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }

        videoRepository.delete(video);
    }
}
