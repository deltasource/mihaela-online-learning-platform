package eu.deltasource.elearning.service;

import eu.deltasource.elearning.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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
        String title = "Welcome to E-Learning Platform!";
        String message = String.format("Hello %s! Welcome to our e-learning platform. " +
                "Start exploring courses and begin your learning journey today!", userName);

        notificationService.createSystemNotification(
                userId, title, message, Notification.NotificationType.WELCOME_MESSAGE);
    }

    public void createEnrollmentNotification(UUID studentId, UUID courseId, String courseName) {
        String title = "Course Enrollment Confirmed";
        String message = String.format("You have successfully enrolled in the course: %s. " +
                "Start learning now!", courseName);

        notificationService.createCourseNotification(
                studentId, courseId, title, message, Notification.NotificationType.COURSE_ENROLLMENT);
    }

    public void createNewVideoNotification(UUID studentId, UUID courseId, String courseName, String videoTitle) {
        String title = "New Video Available";
        String message = String.format("A new video '%s' has been added to your course: %s",
                videoTitle, courseName);

        notificationService.createCourseNotification(
                studentId, courseId, title, message, Notification.NotificationType.NEW_VIDEO_UPLOADED);
    }

    public void createCourseCompletionNotification(UUID studentId, UUID courseId, String courseName) {
        String title = "Congratulations! Course Completed";
        String message = String.format("You have successfully completed the course: %s. " +
                "Great job on your learning achievement!", courseName);

        notificationService.createCourseNotification(
                studentId, courseId, title, message, Notification.NotificationType.COURSE_COMPLETION);
    }

    public void createCourseUpdateNotification(UUID studentId, UUID courseId, String courseName, String updateDetails) {
        String title = "Course Update";
        String message = String.format("The course '%s' has been updated: %s", courseName, updateDetails);

        notificationService.createCourseNotification(
                studentId, courseId, title, message, Notification.NotificationType.COURSE_UPDATE);
    }

    public void createSystemAnnouncementNotification(UUID userId, String title, String message) {
        notificationService.createSystemNotification(
                userId, title, message, Notification.NotificationType.SYSTEM_ANNOUNCEMENT);
    }
}
