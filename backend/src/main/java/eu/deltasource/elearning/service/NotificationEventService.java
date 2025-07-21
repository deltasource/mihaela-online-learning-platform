package eu.deltasource.elearning.service;

import eu.deltasource.elearning.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service to handle automatic notification creation based on system events
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventService {

    private final NotificationService notificationService;

    public void createWelcomeNotification(UUID userId, String userName) {
        log.info("Creating welcome notification for user ID: {}, Name: {}", userId, userName);
        String title = "Welcome to E-Learning Platform!";
        log.debug("Notification title: {}", title);
        String message = String.format("Hello %s! Welcome to our e-learning platform. " +
                "Start exploring courses and begin your learning journey today!", userName);
        log.debug("Notification message: {}", message);
        notificationService.createSystemNotification(
                userId, title, message, Notification.NotificationType.WELCOME_MESSAGE);
    }

    public void createEnrollmentNotification(UUID studentId, UUID courseId, String courseName) {
        log.info("Creating enrollment notification for student ID: {}, Course ID: {}, Course Name: {}",
                studentId, courseId, courseName);
        String title = "Course Enrollment Confirmed";
        log.debug("Notification title: {}", title);
        String message = String.format("You have successfully enrolled in the course: %s. " +
                "Start learning now!", courseName);
        log.debug("Notification message: {}", message);
        notificationService.createCourseNotification(
                studentId, courseId, title, message, Notification.NotificationType.COURSE_ENROLLMENT);
    }

    public void createNewVideoNotification(UUID studentId, UUID courseId, String courseName, String videoTitle) {
        log.info("Creating new video notification for student ID: {}, Course ID: {}, Course Name: {}, Video Title: {}",
                studentId, courseId, courseName, videoTitle);
        String title = "New Video Available";
        log.debug("Notification title: {}", title);
        String message = String.format("A new video '%s' has been added to your course: %s",
                videoTitle, courseName);

        notificationService.createCourseNotification(
                studentId, courseId, title, message, Notification.NotificationType.NEW_VIDEO_UPLOADED);
    }

    public void createCourseCompletionNotification(UUID studentId, UUID courseId, String courseName) {
        log.info("Creating course completion notification for student ID: {}, Course ID: {}, Course Name: {}",
                studentId, courseId, courseName);
        String title = "Congratulations! Course Completed";
        log.debug("Notification title: {}", title);
        String message = String.format("You have successfully completed the course: %s. " +
                "Great job on your learning achievement!", courseName);

        notificationService.createCourseNotification(
                studentId, courseId, title, message, Notification.NotificationType.COURSE_COMPLETION);
    }

    public void createCourseUpdateNotification(UUID studentId, UUID courseId, String courseName, String updateDetails) {
        log.info("Creating course update notification for student ID: {}, Course ID: {}, Course Name: {}, Update Details: {}",
                studentId, courseId, courseName, updateDetails);
        String title = "Course Update";
        String message = String.format("The course '%s' has been updated: %s", courseName, updateDetails);

        notificationService.createCourseNotification(
                studentId, courseId, title, message, Notification.NotificationType.COURSE_UPDATE);
    }

    public void createSystemAnnouncementNotification(UUID userId, String title, String message) {
        log.info("Creating system announcement notification for user ID: {}, Title: {}", userId, title);
        notificationService.createSystemNotification(
                userId, title, message, Notification.NotificationType.SYSTEM_ANNOUNCEMENT);
    }
}
