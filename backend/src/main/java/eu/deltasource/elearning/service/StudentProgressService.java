package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.StudentProgressDTO;
import eu.deltasource.elearning.exception.StudentProgressNotFoundException;
import eu.deltasource.elearning.model.Student;
import eu.deltasource.elearning.model.StudentProgress;
import eu.deltasource.elearning.model.Video;
import eu.deltasource.elearning.model.WatchedVideos;
import eu.deltasource.elearning.repository.StudentProgressRepository;
import eu.deltasource.elearning.repository.WatchedVideosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentProgressService {

    private final StudentService studentService;
    private final CourseService courseService;
    private final VideoService videoService;
    private final StudentProgressRepository studentProgressRepository;
    private final WatchedVideosRepository watchedVideosRepository;

    public StudentProgressDTO getProgressPercentage(UUID studentId, UUID courseId) {
        log.info("Retrieving progress percentage for student ID: {} in course ID: {}", studentId, courseId);
        studentService.getStudentById(studentId);
        courseService.getCourseById(courseId);
        StudentProgress progress = findStudentProgressOrThrow(studentId, courseId);
        return mapToStudentProgressDTO(progress, studentId, courseId);
    }

    private StudentProgress findStudentProgressOrThrow(UUID studentId, UUID courseId) {
        return studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new StudentProgressNotFoundException(
                        String.format("No progress found for student ID %s in course ID %s", studentId, courseId)));
    }

    @Transactional
    public void updateProgress(UUID studentId, UUID courseId, UUID videoId) {
        Video video = videoService.getVideoById(videoId);
        StudentProgress progress = findOrCreateStudentProgress(studentId, courseId);
        markVideoAsWatched(progress, video);
        incrementAndSaveProgress(progress);
    }

    private StudentProgress findOrCreateStudentProgress(UUID studentId, UUID courseId) {
        Optional<StudentProgress> studentProgressOpt = studentProgressRepository.findByStudentIdAndCourseId(studentId, courseId);
        log.info("Checking progress for student ID: {} in course ID: {}", studentId, courseId);
        return studentProgressOpt.orElseGet(() -> createNewStudentProgress(studentId, courseId));
    }

    private StudentProgress createNewStudentProgress(UUID studentId, UUID courseId) {
        Student student = studentService.getStudentById(studentId);
        StudentProgress progress = new StudentProgress();
        progress.setStudent(student);
        progress.setVideosWatched(0);
        progress.setProgressPercentage(0);
        return progress;
    }

    private void markVideoAsWatched(StudentProgress progress, Video video) {
        WatchedVideos watchedVideo = new WatchedVideos();
        watchedVideo.setStudentProgress(progress);
        watchedVideo.setVideo(video);
        watchedVideo.setWatched(true);
        watchedVideosRepository.save(watchedVideo);
    }

    private void incrementAndSaveProgress(StudentProgress progress) {
        progress.setVideosWatched(progress.getVideosWatched() + 1);
        calculateProgressPercentage(progress);
        studentProgressRepository.save(progress);
    }

    private void calculateProgressPercentage(StudentProgress progress) {
        log.info("Calculating progress percentage for student progress: {}", progress);
        if (progress.getTotalVideos() > 0) {
            log.info("Total videos: {}, Videos watched: {}", progress.getTotalVideos(), progress.getVideosWatched());
            double percentage = (progress.getVideosWatched() * 100.0) / progress.getTotalVideos();
            progress.setProgressPercentage(percentage);
        } else {
            log.warn("Total videos is zero, setting progress percentage to 0");
            progress.setProgressPercentage(0);
        }
    }

    private StudentProgressDTO mapToStudentProgressDTO(StudentProgress progress, UUID studentId, UUID courseId) {
        StudentProgressDTO dto = new StudentProgressDTO();
        dto.setStudentId(studentId.toString());
        dto.setCourseId(courseId.toString());
        dto.setProgressPercentage(progress.getProgressPercentage());
        dto.setTotalVideos(progress.getTotalVideos());
        dto.setVideosWatched(progress.getVideosWatched());
        return dto;
    }
}
