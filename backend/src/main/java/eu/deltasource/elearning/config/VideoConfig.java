package eu.deltasource.elearning.config;

import eu.deltasource.elearning.exception.InvalidVideoFormatException;
import eu.deltasource.elearning.exception.VideoOperationException;
import eu.deltasource.elearning.exception.VideoUploadException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration class for handling video file operations such as validation, saving, and deleting video files.
 * This class provides functionality to ensure the uploaded video files meet the required constraints
 * (e.g., size, type), generate file paths for storing them, and perform file operations like saving
 * and deleting the video files.
 *
 * <p>Key features:</p>
 * <ul>
 *     <li>Validates uploaded video files (checks file size and content type).</li>
 *     <li>Generates file paths for saving video files.</li>
 *     <li>Saves video files to the configured directory.</li>
 *     <li>Deletes video files when necessary.</li>
 * </ul>
 *
 * <p>Note: The directory for storing videos is configurable via the {@code video.upload.directory}
 * property in the application's configuration files (e.g., application.properties or application.yml).</p>
 */
@Data
@Slf4j
@Component
public class VideoConfig {

    @Value("${video.upload.directory}")
    private String videoUploadDirectory;

    public static final long BYTES_IN_GB = 1024L * 1024L * 1024L;
    public static final long MAX_FILE_SIZE = 10L * BYTES_IN_GB;

    public void validateVideoFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new VideoOperationException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new VideoUploadException("File size exceeds the maximum allowed size of 10 GB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            throw new InvalidVideoFormatException("Only video files are allowed");
        }
    }

    public String generateFilePath(String originalFilename) {
        String fileName = originalFilename != null ? originalFilename : "video.mp4";
        return Paths.get(videoUploadDirectory, fileName).toString();
    }

    public void saveVideoFile(MultipartFile file, String filePath) {
        try {
            File destinationFile = new File(filePath);

            if (destinationFile.exists()) {
                log.error("File already exists at: {}", filePath);
                throw new VideoOperationException("File already exists and will not be overwritten: " + filePath);
            }

            Path parentDir = Paths.get(filePath).getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            Files.copy(file.getInputStream(), destinationFile.toPath());
            log.info("Video file saved successfully at: {}", filePath);

        } catch (IOException e) {
            log.error("Failed to save video file: {}", e.getMessage());
            throw new VideoOperationException("Failed to save video file", e);
        }
    }

    public void deleteVideoFile(String filePath) {
        try {
            File file = new File(filePath);
            boolean deleted = file.exists() && file.delete();
            log.info("Video file {}: {}", deleted ? "deleted successfully" : "could not be deleted", filePath);
        } catch (Exception e) {
            log.error("Error deleting video file: {}", e.getMessage());
            throw new VideoOperationException("Failed to delete video file", e);
        }
    }
}
