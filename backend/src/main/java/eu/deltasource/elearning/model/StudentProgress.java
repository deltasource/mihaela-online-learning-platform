package eu.deltasource.elearning.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "student_progress")
public class StudentProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @Column(nullable = false)
    private int totalVideos;

    @Column(nullable = false)
    private int videosWatched;

    @OneToMany(mappedBy = "studentProgress")
    private List<WatchedVideos> watchedVideos;

    @Column(nullable = false)
    private double progressPercentage;

    public void updateProgress() {
        if (totalVideos > 0) {
            this.progressPercentage = (videosWatched * 100.0) / totalVideos;
        } else {
            this.progressPercentage = 0;
        }
    }
}
