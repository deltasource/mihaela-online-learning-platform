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
@Table(name = "analytics")
public class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private Video video;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    private Long duration;

    @Column(length = 1000)
    private String metadata;

    private String ipAddress;

    @Column(length = 500)
    private String userAgent;

    private String sessionId;

    private Double completionPercentage;

    private Double score;

    public enum EventType {
        USER_LOGIN,
        USER_LOGOUT,
        USER_REGISTRATION,
        COURSE_VIEW,
        COURSE_ENROLLMENT,
        COURSE_COMPLETION,
        COURSE_DROPOUT,
        VIDEO_START,
        VIDEO_PAUSE,
        VIDEO_RESUME,
        VIDEO_COMPLETE,
        VIDEO_SEEK,
        LESSON_START,
        LESSON_COMPLETE,
        QUIZ_START,
        QUIZ_COMPLETE,
        ASSIGNMENT_SUBMIT,
        COMMENT_POST,
        RATING_SUBMIT,
        BOOKMARK_ADD,
        DOWNLOAD_RESOURCE,
        SEARCH_QUERY,
        PAGE_VIEW,
        ERROR_OCCURRED
    }
}
