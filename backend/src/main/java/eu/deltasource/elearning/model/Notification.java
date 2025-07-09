package eu.deltasource.elearning.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationPriority priority = NotificationPriority.NORMAL;

    @Column(nullable = false)
    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime readAt;

    private UUID relatedEntityId;

    @Enumerated(EnumType.STRING)
    private RelatedEntityType relatedEntityType;

    private String actionUrl;

    public enum NotificationType {
        COURSE_ENROLLMENT,
        COURSE_COMPLETION,
        NEW_VIDEO_UPLOADED,
        ASSIGNMENT_DUE,
        GRADE_POSTED,
        SYSTEM_ANNOUNCEMENT,
        COURSE_UPDATE,
        PAYMENT_CONFIRMATION,
        WELCOME_MESSAGE,
        REMINDER
    }

    public enum NotificationPriority {
        LOW, NORMAL, HIGH, URGENT
    }

    public enum RelatedEntityType {
        COURSE, VIDEO, ASSIGNMENT, ENROLLMENT, USER
    }
}
