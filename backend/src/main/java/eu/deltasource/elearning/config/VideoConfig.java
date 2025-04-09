package eu.deltasource.elearning.config;

import eu.deltasource.elearning.exception.InvalidVideoFormatException;
import eu.deltasource.elearning.exception.VideoOperationException;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class VideoConfig {
    private static final Logger logger = LoggerFactory.getLogger(VideoConfig.class);

    @Value("${video.upload.directory}")
    private String videoUploadDirectory;

    public static final long BYTES_IN_GB = 1024L * 1024L * 1024L;
    public static final long MAX_FILE_SIZE = 10L * BYTES_IN_GB;

    private static final Set<String> ALLOWED_VIDEO_FORMATS = new HashSet<>(
            Arrays.asList("video/mp4", "video/avi", "video/mpeg", "video/quicktime", "video/x-matroska")
    );

    /**
     * Validates if the file is a valid video file
     * @param file The file to validate
     * @throws InvalidVideoFormatException If the file is invalid
     */
    public void validateVideoFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidVideoFormatException("File cannot be empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidVideoFormatException("File size exceeds the maximum allowed size of 10 GB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            throw new InvalidVideoFormatException("Only video files are allowed");
        }
    }

    /**
     * Gets the upload directory for videos
     * @return The directory path
     */
    public String getVideoUploadDirectory() {
        return videoUploadDirectory;
    }

    /**
     * Generates a file path for storing a video
     * @param originalFilename The original filename
     * @return The full file path
     */
    public String generateFilePath(String originalFilename) {
        String fileName = originalFilename != null ? originalFilename : "video.mp4";
        return Paths.get(videoUploadDirectory, fileName).toString();
    }

    /**
     * Saves a video file to disk
     * @param file The multipart file
     * @param filePath The path where to save the file
     * @throws VideoOperationException If an error occurs during file saving
     */
    public void saveVideoFile(MultipartFile file, String filePath) {
        try {
            File destinationFile = new File(filePath);
            Path parentDir = Paths.get(filePath).getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            Files.copy(file.getInputStream(), destinationFile.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            logger.info("Video file saved successfully at: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to save video file: {}", e.getMessage());
            throw new VideoOperationException("Failed to save video file", e);
        }
    }

    /**
     * Deletes a video file from disk
     * @param filePath The path of the file to delete
     * @return true if the file was deleted successfully, false otherwise
     */
    @NotNull
    public boolean deleteVideoFile(String filePath) {
        try {
            File file = new File(filePath);
            boolean deleted = file.exists() && file.delete();
            if (deleted) {
                logger.info("Video file deleted successfully: {}", filePath);
            } else {
                logger.warn("Video file could not be deleted: {}", filePath);
            }
            return deleted;
        } catch (Exception e) {
            logger.error("Error deleting video file: {}", e.getMessage());
            throw new VideoOperationException("Failed to delete video file", e);
        }
    }
}
