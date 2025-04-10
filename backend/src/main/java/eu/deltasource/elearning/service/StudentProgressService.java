package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.StudentProgressDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.StudentNotFoundException;
import eu.deltasource.elearning.exception.VideoNotFoundException;
import eu.deltasource.elearning.model.*;
import eu.deltasource.elearning.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class StudentProgressService {
    
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentProgressRepository studentProgressRepository;
    private final VideoRepository videoRepository;
    private final WatchedVideosRepository watchedVideosRepository;

    public StudentProgressService(StudentRepository studentRepository, CourseRepository courseRepository,
                                  StudentProgressRepository studentProgressRepository,
                                  VideoRepository videoRepository,
                                  WatchedVideosRepository watchedVideosRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentProgressRepository = studentProgressRepository;
        this.videoRepository = videoRepository;
        this.watchedVideosRepository = watchedVideosRepository;
    }

    public StudentProgressDTO getProgressPercentage(UUID studentId, UUID courseId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new StudentNotFoundException("Student with ID " + studentId + " not found");
        }

        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            throw new StudentNotFoundException("Course with ID " + courseId + " not found");
        }

        Optional<StudentProgress> progress = studentProgressRepository.findByStudentAndCourse(student.get(), course.get());

        StudentProgressDTO studentProgressDTO = getStudentProgressDTO(studentId, courseId, progress);

        return studentProgressDTO;
    }

    private static StudentProgressDTO getStudentProgressDTO(UUID studentId, UUID courseId, Optional<StudentProgress> progress) {
        if (progress.isEmpty()) {
            throw new StudentNotFoundException("No progress found for student with ID " + studentId + " in course " + courseId);
        }

        StudentProgress studentProgress = progress.get();

        StudentProgressDTO studentProgressDTO = new StudentProgressDTO();
        studentProgressDTO.setStudentId(studentId.toString());
        studentProgressDTO.setCourseId(courseId.toString());
        studentProgressDTO.setProgressPercentage(studentProgress.getProgressPercentage());
        studentProgressDTO.setTotalVideos(studentProgress.getTotalVideos());
        studentProgressDTO.setVideosWatched(studentProgress.getVideosWatched());
        return studentProgressDTO;
    }

    public void updateProgress(UUID studentId, UUID courseId, UUID videoId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new StudentNotFoundException("Student with ID " + studentId + " not found");
        }

        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            throw new CourseNotFoundException("Course with ID " + courseId + " not found");
        }

        Optional<Video> video = videoRepository.findById(videoId);
        if (video.isEmpty()) {
            throw new VideoNotFoundException("Video with ID " + videoId + " not found");
        }

        Optional<StudentProgress> studentProgress = studentProgressRepository.findByStudentAndCourse(student.get(), course.get());
        StudentProgress progress;

        if (studentProgress.isPresent()) {
            progress = studentProgress.get();
        } else {
            progress = new StudentProgress();
            progress.setStudent(student.get());
            progress.setCourse(course.get());
            progress.setTotalVideos(course.get().getVideos().size());
            progress.setVideosWatched(0);
        }

        WatchedVideos watchedVideo = new WatchedVideos();
        watchedVideo.setStudentProgress(progress);
        watchedVideo.setVideo(video.get());
        watchedVideo.setWatched(true); 
        watchedVideosRepository.save(watchedVideo);

        progress.setVideosWatched(progress.getVideosWatched() + 1);  
        progress.updateProgress(); 
        studentProgressRepository.save(progress);
    }
}
