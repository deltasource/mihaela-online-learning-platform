package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.LessonDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.LessonNotFoundException;
import eu.deltasource.elearning.model.Course;
import eu.deltasource.elearning.model.Lesson;
import eu.deltasource.elearning.repository.CourseRepository;
import eu.deltasource.elearning.repository.LessonRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public LessonDTO createLesson(LessonDTO lessonDTO) {
        log.info("Creating lesson: {}", lessonDTO);
        UUID courseId = lessonDTO.getCourseId();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        Lesson lesson = new Lesson();
        lesson.setId(UUID.randomUUID());
        lesson.setCourse(course);
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setDescription(lessonDTO.getDescription());
        lesson.setContent(lessonDTO.getContent());
        lesson.setVideoUrl(lessonDTO.getVideoUrl());
        lesson.setDuration(lessonDTO.getDuration());
        lesson.setOrder(lessonDTO.getOrder());
        lesson.setPublished(lessonDTO.isPublished());
        lesson.setCreatedAt(LocalDateTime.now());
        lesson.setUpdatedAt(LocalDateTime.now());

        log.info("Saving lesson: {}", lesson);
        lesson = lessonRepository.save(lesson);
        log.info("Lesson created with ID: {}", lesson.getId());
        return mapToLessonDTO(lesson);
    }

    public LessonDTO getLessonById(UUID lessonId) {
        log.info("Retrieving lesson with ID: {}", lessonId);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found"));
        return mapToLessonDTO(lesson);
    }

    public List<LessonDTO> getLessonsByCourseId(UUID courseId) {
        log.info("Retrieving lessons for course ID: {}", courseId);
        UUID courseUuid = UUID.fromString(courseId.toString());
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByOrderAsc(courseUuid);
        return lessons.stream()
                .map(this::mapToLessonDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LessonDTO updateLesson(UUID lessonId, LessonDTO lessonDTO) {
        log.info("Updating lesson with ID: {}", lessonId);
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found"));
        log.info("Found existing lesson: {}", existingLesson);
        if (!existingLesson.getCourse().getId().equals(lessonDTO.getCourseId())) {
            log.warn("Course ID mismatch for lesson update. Existing Course ID: {}, New Course ID: {}",
                    existingLesson.getCourse().getId(), lessonDTO.getCourseId());
            UUID courseId = lessonDTO.getCourseId();
            log.info("Fetching new course with ID: {}", courseId);
            Course newCourse = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CourseNotFoundException("Course not found"));
            existingLesson.setCourse(newCourse);
        }

        existingLesson.setTitle(lessonDTO.getTitle());
        existingLesson.setDescription(lessonDTO.getDescription());
        existingLesson.setContent(lessonDTO.getContent());
        existingLesson.setVideoUrl(lessonDTO.getVideoUrl());
        existingLesson.setDuration(lessonDTO.getDuration());
        existingLesson.setOrder(lessonDTO.getOrder());
        existingLesson.setPublished(lessonDTO.isPublished());
        existingLesson.setUpdatedAt(LocalDateTime.now());

        log.info("Saving updated lesson: {}", existingLesson);
        existingLesson = lessonRepository.save(existingLesson);
        log.info("Lesson updated with ID: {}", existingLesson.getId());
        return mapToLessonDTO(existingLesson);
    }

    @Transactional
    public void deleteLesson(UUID lessonId) {
        log.info("Deleting lesson with ID: {}", lessonId);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found"));
        lessonRepository.delete(lesson);
    }

    @NotNull
    private LessonDTO mapToLessonDTO(Lesson lesson) {
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setId(lesson.getId());
        lessonDTO.setCourseId(lesson.getCourse().getId());
        lessonDTO.setTitle(lesson.getTitle());
        lessonDTO.setDescription(lesson.getDescription());
        lessonDTO.setContent(lesson.getContent());
        lessonDTO.setVideoUrl(lesson.getVideoUrl());
        lessonDTO.setDuration(lesson.getDuration());
        lessonDTO.setOrder(lesson.getOrder());
        lessonDTO.setPublished(lesson.isPublished());
        lessonDTO.setCreatedAt(lesson.getCreatedAt());
        lessonDTO.setUpdatedAt(lesson.getUpdatedAt());
        return lessonDTO;
    }
}
