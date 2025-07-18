package eu.deltasource.elearning.service;

import eu.deltasource.elearning.model.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationEventServiceTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationEventService notificationEventService;

    @Test
    void givenUserIdAndName_whenCreateWelcomeNotification_thenDelegatesToNotificationService() {
        // Given
        UUID userId = UUID.randomUUID();
        String userName = "Bobi";

        // When
        notificationEventService.createWelcomeNotification(userId, userName);

        // Then
        verify(notificationService).createSystemNotification(
                eq(userId),
                eq("Welcome to E-Learning Platform!"),
                contains("Hello Bobi!"),
                eq(Notification.NotificationType.WELCOME_MESSAGE)
        );
    }

    @Test
    void givenStudentIdCourseIdAndName_whenCreateEnrollmentNotification_thenDelegatesToNotificationService() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        String courseName = "Math for Programmers";

        // When
        notificationEventService.createEnrollmentNotification(studentId, courseId, courseName);

        // Then
        verify(notificationService).createCourseNotification(
                eq(studentId),
                eq(courseId),
                eq("Course Enrollment Confirmed"),
                contains("successfully enrolled in the course: Math for Programmers"),
                eq(Notification.NotificationType.COURSE_ENROLLMENT)
        );
    }

    @Test
    void givenStudentIdCourseIdCourseNameVideoTitle_whenCreateNewVideoNotification_thenDelegatesToNotificationService() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        String courseName = "Java Programming Basics";
        String videoTitle = "Introduction";

        // When
        notificationEventService.createNewVideoNotification(studentId, courseId, courseName, videoTitle);

        // Then
        verify(notificationService).createCourseNotification(
                eq(studentId),
                eq(courseId),
                eq("New Video Available"),
                contains("A new video 'Introduction' has been added to your course: Java Programming Basics"),
                eq(Notification.NotificationType.NEW_VIDEO_UPLOADED)
        );
    }

    @Test
    void givenStudentIdCourseIdAndName_whenCreateCourseCompletionNotification_thenDelegatesToNotificationService() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        String courseName = "Database Management Systems";

        // When
        notificationEventService.createCourseCompletionNotification(studentId, courseId, courseName);

        // Then
        verify(notificationService).createCourseNotification(
                eq(studentId),
                eq(courseId),
                eq("Congratulations! Course Completed"),
                contains("completed the course: Database Management Systems. Great job on your learning achievement!"),
                eq(Notification.NotificationType.COURSE_COMPLETION)
        );
    }

    @Test
    void givenStudentIdCourseIdCourseNameUpdateDetails_whenCreateCourseUpdateNotification_thenDelegatesToNotificationService() {
        // Given
        UUID studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        String courseName = "AWS Cloud Fundamentals";
        String updateDetails = "New chapter added";

        // When
        notificationEventService.createCourseUpdateNotification(studentId, courseId, courseName, updateDetails);

        // Then
        verify(notificationService).createCourseNotification(
                eq(studentId),
                eq(courseId),
                eq("Course Update"),
                contains("The course 'AWS Cloud Fundamentals' has been updated: New chapter added"),
                eq(Notification.NotificationType.COURSE_UPDATE)
        );
    }

    @Test
    void givenUserIdTitleMessage_whenCreateSystemAnnouncementNotification_thenDelegatesToNotificationService() {
        // Given
        UUID userId = UUID.randomUUID();
        String title = "System Maintenance";
        String message = "The platform will be down tonight.";

        // When
        notificationEventService.createSystemAnnouncementNotification(userId, title, message);

        // Then
        verify(notificationService).createSystemNotification(
                eq(userId),
                eq(title),
                eq(message),
                eq(Notification.NotificationType.SYSTEM_ANNOUNCEMENT)
        );
    }
}
