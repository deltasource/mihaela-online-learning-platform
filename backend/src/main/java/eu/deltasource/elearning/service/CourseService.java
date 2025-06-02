package eu.deltasource.elearning.service;

import eu.deltasource.elearning.DTOs.CourseDTO;
import eu.deltasource.elearning.DTOs.LessonDTO;
import eu.deltasource.elearning.DTOs.SectionDTO;
import eu.deltasource.elearning.exception.CourseNotFoundException;
import eu.deltasource.elearning.model.*;
import eu.deltasource.elearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final SectionRepository sectionRepository;
    private final LessonRepository lessonRepository;
    private final VideoRepository videoRepository;
    private final Random random = new Random();

    @Autowired
    public CourseService(
            CourseRepository courseRepository,
            InstructorRepository instructorRepository,
            SectionRepository sectionRepository,
            LessonRepository lessonRepository,
            VideoRepository videoRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.sectionRepository = sectionRepository;
        this.lessonRepository = lessonRepository;
        this.videoRepository = videoRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseDTO getCourseById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
        return mapToDetailedCourseDTO(course);
    }

    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        try {
            System.out.println("Creating course with instructor ID: " + courseDTO.getInstructorId());

            // Find or create instructor
            Instructor instructor = findOrCreateInstructor(courseDTO.getInstructorId());
            System.out.println("Found/created instructor: " + instructor.getId() + " - " + instructor.getEmail());

            // Create course
            Course course = new Course();
            course.setName(courseDTO.getName() != null ? courseDTO.getName() : courseDTO.getTitle());
            course.setDescription(courseDTO.getDescription());
            course.setInstructor(instructor);
            course.setCategory(courseDTO.getCategory() != null ? courseDTO.getCategory() : "General");
            course.setLevel(courseDTO.getLevel() != null ? courseDTO.getLevel() : "beginner");
            course.setPrice(courseDTO.getPrice() != null ? courseDTO.getPrice() : 0.0);
            course.setRating(courseDTO.getRating() != null ? courseDTO.getRating() : 0.0);

            // Handle thumbnail URL
            if (courseDTO.getThumbnail() != null && courseDTO.getThumbnail().length() > 1990) {
                course.setThumbnail(courseDTO.getThumbnail().substring(0, 1990));
            } else {
                course.setThumbnail(courseDTO.getThumbnail());
            }

            course.setCreatedAt(LocalDateTime.now());
            course.setUpdatedAt(LocalDateTime.now());

            System.out.println("Saving course: " + course.getName());
            Course savedCourse = courseRepository.save(course);
            System.out.println("Course saved with ID: " + savedCourse.getId());

            // Create sections and lessons
            createSectionsAndLessons(savedCourse, courseDTO.getSections());

            return mapToDetailedCourseDTO(savedCourse);

        } catch (Exception e) {
            System.err.println("Error creating course: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create course: " + e.getMessage(), e);
        }
    }

    private Instructor findOrCreateInstructor(UUID instructorId) {
        if (instructorId == null) {
            throw new RuntimeException("Instructor ID is required");
        }

        System.out.println("Looking for instructor with ID: " + instructorId);

        // Try to find existing instructor
        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);

        if (instructor != null) {
            System.out.println("Found existing instructor: " + instructor.getEmail());
            return instructor;
        }

        System.out.println("Instructor not found, checking default instructor...");

        // Try to find default instructor
        String defaultEmail = "eray.ali@example.com";
        instructor = instructorRepository.findByEmail(defaultEmail).orElse(null);

        if (instructor != null) {
            System.out.println("Found default instructor: " + instructor.getId());
            return instructor;
        }

        System.out.println("Default instructor not found, creating new one...");

        // Create new instructor with unique email
        instructor = new Instructor();

        // Generate a truly unique email with timestamp and random number
        String uniqueEmail = "instructor_" + System.currentTimeMillis() + "_" + random.nextInt(10000) + "@example.com";
        instructor.setEmail(uniqueEmail);
        instructor.setFirstName("Instructor");
        instructor.setLastName("User");
        instructor.setDepartment("Computer Science");

        try {
            System.out.println("Attempting to save instructor with email: " + instructor.getEmail());
            instructor = instructorRepository.save(instructor);
            System.out.println("Successfully created instructor with ID: " + instructor.getId());
            return instructor;
        } catch (Exception e) {
            System.err.println("Failed to save instructor: " + e.getMessage());
            e.printStackTrace();

            // Last resort: create with default email
            try {
                instructor = new Instructor();
                instructor.setEmail(defaultEmail);
                instructor.setFirstName("Eray");
                instructor.setLastName("Ali");
                instructor.setDepartment("Computer Science");

                System.out.println("Creating instructor with default email...");
                instructor = instructorRepository.save(instructor);
                System.out.println("Successfully created default instructor: " + instructor.getId());
                return instructor;
            } catch (Exception ex) {
                System.err.println("Failed to create default instructor: " + ex.getMessage());
                ex.printStackTrace();

                // Final attempt: find any instructor
                List<Instructor> instructors = instructorRepository.findAll();
                if (!instructors.isEmpty()) {
                    System.out.println("Using existing instructor as fallback: " + instructors.get(0).getId());
                    return instructors.get(0);
                }

                throw new RuntimeException("Failed to create or find any instructor: " + ex.getMessage());
            }
        }
    }

    private void createSectionsAndLessons(Course course, List<SectionDTO> sectionDTOs) {
        System.out.println("Creating sections and lessons for course: " + course.getId());

        if (sectionDTOs == null || sectionDTOs.isEmpty()) {
            // Create default section
            System.out.println("No sections provided, creating default section");
            Section defaultSection = new Section();
            defaultSection.setTitle("Course Content");
            defaultSection.setCourse(course);
            defaultSection.setOrder(1);
            sectionRepository.save(defaultSection);
            System.out.println("Default section created");
        } else {
            // Create sections and lessons
            System.out.println("Creating " + sectionDTOs.size() + " sections");
            for (int i = 0; i < sectionDTOs.size(); i++) {
                SectionDTO sectionDTO = sectionDTOs.get(i);

                Section section = new Section();
                section.setTitle(sectionDTO.getTitle());
                section.setCourse(course);
                section.setOrder(sectionDTO.getOrder() != null ? sectionDTO.getOrder() : (i + 1));
                Section savedSection = sectionRepository.save(section);
                System.out.println("Section created: " + savedSection.getTitle());

                // Create lessons for this section
                if (sectionDTO.getLessons() != null && !sectionDTO.getLessons().isEmpty()) {
                    System.out.println("Creating " + sectionDTO.getLessons().size() + " lessons for section: " + savedSection.getTitle());
                    for (int j = 0; j < sectionDTO.getLessons().size(); j++) {
                        LessonDTO lessonDTO = sectionDTO.getLessons().get(j);

                        Lesson lesson = new Lesson();
                        lesson.setTitle(lessonDTO.getTitle());
                        lesson.setDescription(lessonDTO.getDescription());

                        // Handle video URL
                        if (lessonDTO.getVideoUrl() != null && lessonDTO.getVideoUrl().length() > 1990) {
                            lesson.setVideoUrl(lessonDTO.getVideoUrl().substring(0, 1990));
                        } else {
                            lesson.setVideoUrl(lessonDTO.getVideoUrl());
                        }

                        lesson.setDuration(lessonDTO.getDuration() != null ? lessonDTO.getDuration() : 0);
                        lesson.setSection(savedSection);
                        lesson.setOrder(lessonDTO.getOrder() != null ? lessonDTO.getOrder() : (j + 1));
                        lesson.setIsPreview(lessonDTO.getIsPreview() != null ? lessonDTO.getIsPreview() : false);

                        lessonRepository.save(lesson);
                        System.out.println("Lesson created: " + lesson.getTitle());
                    }
                }
            }
        }
        System.out.println("Finished creating sections and lessons");
    }

    @Transactional
    public CourseDTO updateCourse(UUID id, CourseDTO courseDTO) {
        try {
            Course existingCourse = courseRepository.findById(id)
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));

            // Update basic course info
            if (courseDTO.getName() != null) {
                existingCourse.setName(courseDTO.getName());
            } else if (courseDTO.getTitle() != null) {
                existingCourse.setName(courseDTO.getTitle());
            }

            if (courseDTO.getDescription() != null) {
                existingCourse.setDescription(courseDTO.getDescription());
            }

            if (courseDTO.getInstructorId() != null) {
                Instructor instructor = findOrCreateInstructor(courseDTO.getInstructorId());
                existingCourse.setInstructor(instructor);
            }

            if (courseDTO.getCategory() != null) {
                existingCourse.setCategory(courseDTO.getCategory());
            }
            if (courseDTO.getLevel() != null) {
                existingCourse.setLevel(courseDTO.getLevel());
            }
            if (courseDTO.getPrice() != null) {
                existingCourse.setPrice(courseDTO.getPrice());
            }
            if (courseDTO.getRating() != null) {
                existingCourse.setRating(courseDTO.getRating());
            }

            // Handle thumbnail URL
            if (courseDTO.getThumbnail() != null) {
                if (courseDTO.getThumbnail().length() > 1990) {
                    existingCourse.setThumbnail(courseDTO.getThumbnail().substring(0, 1990));
                } else {
                    existingCourse.setThumbnail(courseDTO.getThumbnail());
                }
            }

            existingCourse.setUpdatedAt(LocalDateTime.now());
            Course updatedCourse = courseRepository.save(existingCourse);

            return mapToDetailedCourseDTO(updatedCourse);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update course: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteCourse(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Transactional
    public List<CourseDTO> getCoursesByInstructorEmail(String instructorEmail) {
        System.out.println("=== FETCHING COURSES FOR INSTRUCTOR EMAIL: " + instructorEmail + " ===");

        // First, let's see all courses in the database
        List<Course> allCourses = courseRepository.findAll();
        System.out.println("Total courses in database: " + allCourses.size());

        for (Course course : allCourses) {
            System.out.println("Course: " + course.getId() + " - " + course.getName() +
                    " - Instructor: " + (course.getInstructor() != null ? course.getInstructor().getEmail() : "NULL"));
        }

        // Try to find the instructor by email
        Instructor instructor = instructorRepository.findByEmail(instructorEmail).orElse(null);
        List<Course> courses = new ArrayList<>();

        if (instructor != null) {
            System.out.println("Found instructor with ID: " + instructor.getId() + " for email: " + instructorEmail);
            courses = courseRepository.findByInstructorId(instructor.getId());
            System.out.println("Found " + courses.size() + " courses for instructor ID: " + instructor.getId());
        } else {
            System.out.println("Instructor not found with email: " + instructorEmail);

            // Try to find courses directly by instructor email
            courses = courseRepository.findByInstructorEmail(instructorEmail);
            System.out.println("Found " + courses.size() + " courses directly by instructor email");

            if (courses.isEmpty()) {
                // Check for courses without instructors and assign them
                List<Course> orphanCourses = courseRepository.findCoursesWithoutInstructor();
                System.out.println("Found " + orphanCourses.size() + " courses without instructors");

                if (!orphanCourses.isEmpty()) {
                    // Create instructor for this email
                    instructor = new Instructor();
                    instructor.setEmail(instructorEmail);
                    instructor.setFirstName("Mock");
                    instructor.setLastName("Instructor");
                    instructor.setDepartment("General");
                    instructor = instructorRepository.save(instructor);
                    System.out.println("Created instructor with ID: " + instructor.getId());

                    // Assign all orphan courses to this instructor
                    for (Course orphanCourse : orphanCourses) {
                        orphanCourse.setInstructor(instructor);
                        courseRepository.save(orphanCourse);
                        System.out.println("Assigned course " + orphanCourse.getId() + " to instructor " + instructor.getId());
                    }

                    courses = orphanCourses;
                } else {
                    // Create instructor anyway for future courses
                    instructor = new Instructor();
                    instructor.setEmail(instructorEmail);
                    instructor.setFirstName("Mock");
                    instructor.setLastName("Instructor");
                    instructor.setDepartment("General");
                    instructor = instructorRepository.save(instructor);
                    System.out.println("Created instructor with ID: " + instructor.getId() + " for future courses");
                }
            }
        }

        System.out.println("=== FINAL RESULT: " + courses.size() + " courses for " + instructorEmail + " ===");

        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    @Transactional
    public String fixOrphanCourses(String instructorEmail) {
        System.out.println("=== FIXING ORPHAN COURSES FOR: " + instructorEmail + " ===");

        // Find or create instructor
        Instructor instructor = instructorRepository.findByEmail(instructorEmail).orElse(null);
        if (instructor == null) {
            instructor = new Instructor();
            instructor.setEmail(instructorEmail);
            instructor.setFirstName("Mock");
            instructor.setLastName("Instructor");
            instructor.setDepartment("General");
            instructor = instructorRepository.save(instructor);
            System.out.println("Created instructor: " + instructor.getId());
        }

        // Find courses without instructors
        List<Course> orphanCourses = courseRepository.findCoursesWithoutInstructor();
        System.out.println("Found " + orphanCourses.size() + " orphan courses");

        // Assign them to the instructor
        for (Course course : orphanCourses) {
            course.setInstructor(instructor);
            courseRepository.save(course);
            System.out.println("Assigned course " + course.getId() + " to instructor " + instructor.getId());
        }

        return "Fixed " + orphanCourses.size() + " orphan courses for instructor " + instructorEmail;
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesByInstructorId(UUID instructorId) {
        List<Course> courses = courseRepository.findByInstructorId(instructorId);
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> searchCourses(String query) {
        List<Course> courses = courseRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesByCategory(String category) {
        List<Course> courses = courseRepository.findByCategoryIgnoreCase(category);
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    private CourseDTO mapToCourseDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setTitle(course.getName());
        dto.setDescription(course.getDescription());
        dto.setThumbnail(course.getThumbnail());
        dto.setCategory(course.getCategory());
        dto.setLevel(course.getLevel());
        dto.setPrice(course.getPrice());
        dto.setRating(course.getRating());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());

        if (course.getInstructor() != null) {
            dto.setInstructorId(course.getInstructor().getId());
            dto.setInstructorName(course.getInstructor().getFirstName() + " " + course.getInstructor().getLastName());
        }

        if (course.getStudents() != null) {
            dto.setTotalStudents(course.getStudents().size());
            dto.setStudentIds(course.getStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toList()));
        } else {
            dto.setTotalStudents(0);
        }

        return dto;
    }

    private CourseDTO mapToDetailedCourseDTO(Course course) {
        CourseDTO dto = mapToCourseDTO(course);

        // Add sections and lessons
        List<Section> sections = sectionRepository.findByCourseIdOrderByOrder(course.getId());
        List<SectionDTO> sectionDTOs = new ArrayList<>();

        for (Section section : sections) {
            SectionDTO sectionDTO = new SectionDTO();
            sectionDTO.setId(section.getId().toString());
            sectionDTO.setTitle(section.getTitle());
            sectionDTO.setCourseId(course.getId());
            sectionDTO.setOrder(section.getOrder());

            // Add lessons
            List<Lesson> lessons = lessonRepository.findBySectionIdOrderByOrder(section.getId());
            List<LessonDTO> lessonDTOs = new ArrayList<>();

            for (Lesson lesson : lessons) {
                LessonDTO lessonDTO = new LessonDTO();
                lessonDTO.setId(lesson.getId().toString());
                lessonDTO.setTitle(lesson.getTitle());
                lessonDTO.setDescription(lesson.getDescription());
                lessonDTO.setVideoUrl(lesson.getVideoUrl());
                lessonDTO.setDuration(lesson.getDuration());
                lessonDTO.setSectionId(section.getId().toString());
                lessonDTO.setOrder(lesson.getOrder());
                lessonDTO.setIsPreview(lesson.getIsPreview());

                lessonDTOs.add(lessonDTO);
            }

            sectionDTO.setLessons(lessonDTOs);
            sectionDTOs.add(sectionDTO);
        }

        dto.setSections(sectionDTOs);
        return dto;
    }
}
