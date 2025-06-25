package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.*;
import eu.deltasource.elearning.enums.Level;
import eu.deltasource.elearning.enums.QuestionType;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.exception.InstructorNotFoundException;
import eu.deltasource.elearning.model.*;
import eu.deltasource.elearning.repository.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Instructor instructor = instructorRepository.findById(courseDTO.getInstructorId())
                .orElseThrow(() -> new InstructorNotFoundException("Instructor not found"));
        List<Student> students = courseDTO.getStudentIds() != null ?
                studentRepository.findAllById(courseDTO.getStudentIds()) : List.of();

        Course course = new Course();
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setThumbnail(courseDTO.getThumbnail());
        course.setInstructor(instructor);
        course.setCategory(courseDTO.getCategory());
        course.setLevel(courseDTO.getLevel());
        course.setDuration(courseDTO.getDuration());
        course.setRating(courseDTO.getRating() != null ? courseDTO.getRating() : 0.0);
        course.setRatingCount(courseDTO.getRatingCount() != null ? courseDTO.getRatingCount() : 0);
        course.setEnrollmentCount(courseDTO.getEnrollmentCount() != null ? courseDTO.getEnrollmentCount() : 0);
        course.setPrice(courseDTO.getPrice());
        course.setPublished(courseDTO.getIsPublished() != null ? courseDTO.getIsPublished() : false);
        course.setStudents(students);

        Instant now = Instant.now();
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        course = courseRepository.save(course);

        if (courseDTO.getLessons() != null) {
            saveLessons(course, courseDTO.getLessons());
        }

        if (courseDTO.getQuizzes() != null) {
            saveQuizzes(course, courseDTO.getQuizzes());
        }

        return mapToCourseDTO(courseRepository.findById(course.getId()).orElseThrow());
    }

    public CourseDTO getCourseById(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        return mapToCourseDTO(course);
    }

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    @Transactional
    public CourseDTO updateCourse(UUID courseId, CourseDTO courseDTO) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        Instructor instructor = null;
        if (courseDTO.getInstructorId() != null) {
            instructor = instructorRepository.findById(courseDTO.getInstructorId())
                    .orElseThrow(() -> new InstructorNotFoundException("Instructor not found"));
            existingCourse.setInstructor(instructor);
        }

        if (courseDTO.getName() != null) existingCourse.setName(courseDTO.getName());
        if (courseDTO.getDescription() != null) existingCourse.setDescription(courseDTO.getDescription());
        if (courseDTO.getThumbnail() != null) existingCourse.setThumbnail(courseDTO.getThumbnail());
        if (courseDTO.getCategory() != null) existingCourse.setCategory(courseDTO.getCategory());
        if (courseDTO.getLevel() != null) existingCourse.setLevel(Level.valueOf(courseDTO.getLevel().name()));        if (courseDTO.getDuration() != null) existingCourse.setDuration(courseDTO.getDuration());
        if (courseDTO.getRating() != null) existingCourse.setRating(courseDTO.getRating());
        if (courseDTO.getRatingCount() != null) existingCourse.setRatingCount(courseDTO.getRatingCount());
        if (courseDTO.getEnrollmentCount() != null) existingCourse.setEnrollmentCount(courseDTO.getEnrollmentCount());
        if (courseDTO.getPrice() != null) existingCourse.setPrice(courseDTO.getPrice());
        if (courseDTO.getIsPublished() != null) existingCourse.setPublished(courseDTO.getIsPublished());

        if (courseDTO.getStudentIds() != null) {
            List<Student> students = studentRepository.findAllById(courseDTO.getStudentIds());
            existingCourse.setStudents(students);
        }

        existingCourse.setUpdatedAt(LocalDateTime.now());
        existingCourse = courseRepository.save(existingCourse);

        if (courseDTO.getLessons() != null) {
            updateLessons(existingCourse, courseDTO.getLessons());
        }

        if (courseDTO.getQuizzes() != null) {
            updateQuizzes(existingCourse, courseDTO.getQuizzes());
        }

        return mapToCourseDTO(existingCourse);
    }

    @Transactional
    public void deleteCourse(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    @NotNull
    private CourseDTO mapToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setName(course.getName());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setThumbnail(course.getThumbnail());
        courseDTO.setInstructorId(course.getInstructor().getId());
        courseDTO.setInstructorName(course.getInstructor().getFirstName() + " " +
                course.getInstructor().getLastName());
        courseDTO.setCategory(course.getCategory());
        courseDTO.setLevel(course.getLevel());
        courseDTO.setDuration(course.getDuration());
        courseDTO.setRating(course.getRating());
        courseDTO.setRatingCount(course.getRatingCount());
        courseDTO.setEnrollmentCount(course.getEnrollmentCount());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setIsPublished(course.isPublished());
        courseDTO.setCreatedAt(course.getCreatedAt());
        courseDTO.setUpdatedAt(course.getUpdatedAt());

        courseDTO.setStudentIds(course.getStudents().stream().map(Student::getId).collect(Collectors.toList()));

        if (course.getLessons() != null) {
            courseDTO.setLessons(course.getLessons().stream()
                    .map(this::mapToLessonDTO)
                    .collect(Collectors.toList()));
        }

        if (course.getQuizzes() != null) {
            courseDTO.setQuizzes(course.getQuizzes().stream()
                    .map(this::mapToQuizDTO)
                    .collect(Collectors.toList()));
        }

        return courseDTO;
    }

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
        lessonDTO.setIsPublished(lesson.isPublished());
        lessonDTO.setCreatedAt(lesson.getCreatedAt());
        lessonDTO.setUpdatedAt(lesson.getUpdatedAt());
        return lessonDTO;
    }

    private QuizDTO mapToQuizDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quiz.getId());
        quizDTO.setCourseId(quiz.getCourse().getId());
        quizDTO.setTitle(quiz.getTitle());
        quizDTO.setDescription(quiz.getDescription());
        quizDTO.setPassingScore(quiz.getPassingScore());
        quizDTO.setTimeLimit(quiz.getTimeLimit());
        quizDTO.setOrder(quiz.getOrder());
        quizDTO.setIsPublished(quiz.isPublished());
        quizDTO.setCreatedAt(quiz.getCreatedAt());
        quizDTO.setUpdatedAt(quiz.getUpdatedAt());

        if (quiz.getQuestions() != null) {
            quizDTO.setQuestions(quiz.getQuestions().stream()
                    .map(this::mapToQuestionDTO)
                    .collect(Collectors.toList()));
        }

        return quizDTO;
    }

    private QuestionDTO mapToQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setQuizId(question.getQuiz().getId());
        questionDTO.setQuestion(question.getQuestion());
        questionDTO.setQuestionType(question.getQuestionType().name());
        questionDTO.setPoints(question.getPoints());

        if (question.getOptions() != null) {
            questionDTO.setOptions(question.getOptions().stream()
                    .map(this::mapToOptionDTO)
                    .collect(Collectors.toList()));
        }

        return questionDTO;
    }

    private OptionDTO mapToOptionDTO(Option option) {
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setId(option.getId());
        optionDTO.setQuestionId(option.getQuestion().getId());
        optionDTO.setText(option.getText());
        optionDTO.setIsCorrect(option.isCorrect());
        return optionDTO;
    }

    private void saveLessons(Course course, List<LessonDTO> lessonDTOs) {
        Instant now = Instant.now();
        lessonDTOs.forEach(lessonDTO -> {
            Lesson lesson = new Lesson();
            lesson.setCourse(course);
            lesson.setTitle(lessonDTO.getTitle());
            lesson.setDescription(lessonDTO.getDescription());
            lesson.setContent(lessonDTO.getContent());
            lesson.setVideoUrl(lessonDTO.getVideoUrl());
            lesson.setDuration(lessonDTO.getDuration());
            lesson.setOrder(lessonDTO.getOrder());
            lesson.setPublished(lessonDTO.getIsPublished() != null ? lessonDTO.getIsPublished() : false);
            lesson.setCreatedAt(LocalDateTime.now());
            lesson.setUpdatedAt(LocalDateTime.now());
            lessonRepository.save(lesson);
        });
    }

    private void saveQuizzes(Course course, List<QuizDTO> quizDTOs) {
        Instant now = Instant.now();
        quizDTOs.forEach(quizDTO -> {
            Quiz quiz = new Quiz();
            quiz.setCourse(course);
            quiz.setTitle(quizDTO.getTitle());
            quiz.setDescription(quizDTO.getDescription());
            quiz.setPassingScore(quizDTO.getPassingScore());
            quiz.setTimeLimit(quizDTO.getTimeLimit());
            quiz.setOrder(quizDTO.getOrder());
            quiz.setPublished(quizDTO.getIsPublished() != null ? quizDTO.getIsPublished() : false);
            quiz.setCreatedAt(LocalDateTime.now());
            quiz.setUpdatedAt(LocalDateTime.now());
            quiz = quizRepository.save(quiz);

            if (quizDTO.getQuestions() != null) {
                saveQuestions(quiz, quizDTO.getQuestions());
            }
        });
    }

    private void saveQuestions(Quiz quiz, List<QuestionDTO> questionDTOs) {
        questionDTOs.forEach(questionDTO -> {
            Question question = new Question();
            question.setQuiz(quiz);
            question.setQuestion(questionDTO.getQuestion());
            question.setQuestionType(QuestionType.valueOf(questionDTO.getQuestionType()));
            question.setPoints(questionDTO.getPoints());
            question = questionRepository.save(question);

            if (questionDTO.getOptions() != null) {
                saveOptions(question, questionDTO.getOptions());
            }
        });
    }

    private void saveOptions(Question question, List<OptionDTO> optionDTOs) {
        optionDTOs.forEach(optionDTO -> {
            Option option = new Option();
            option.setQuestion(question);
            option.setText(optionDTO.getText());
            option.setCorrect(optionDTO.getIsCorrect() != null ? optionDTO.getIsCorrect() : false);
            optionRepository.save(option);
        });
    }

    private void updateLessons(Course course, List<LessonDTO> lessonDTOs) {

    }

    private void updateQuizzes(Course course, List<QuizDTO> quizDTOs) {

    }
}
