package eu.deltasource.elearning.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "watched_videos")
public class WatchedVideos {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student_progress_id", referencedColumnName = "id")
    @NotNull(message = "Student progress cannot be null")
    private StudentProgress studentProgress;

    @ManyToOne
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    @NotNull(message = "Video cannot be null")
    private Video video;

    private boolean watched;
}
