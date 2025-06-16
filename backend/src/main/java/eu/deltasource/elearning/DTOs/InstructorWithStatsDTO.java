package eu.deltasource.elearning.DTOs;

import java.util.List;
import java.util.UUID;

public class InstructorWithStatsDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String bio;
    private String profileImage;
    private Integer totalCourses;
    private Integer totalVideos;
    private Integer totalStudents;
    private Double averageRating;
    private List<CourseWithStatsDTO> courses;

    // Constructors
    public InstructorWithStatsDTO() {}

    public InstructorWithStatsDTO(UUID id, String firstName, String lastName, String email, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Integer getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(Integer totalCourses) {
        this.totalCourses = totalCourses;
    }

    public Integer getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(Integer totalVideos) {
        this.totalVideos = totalVideos;
    }

    public Integer getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public List<CourseWithStatsDTO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseWithStatsDTO> courses) {
        this.courses = courses;
    }

    // Inner class for course statistics
    public static class CourseWithStatsDTO {
        private UUID id;
        private String name;
        private String description;
        private String thumbnail;
        private String category;
        private String level;
        private Double price;
        private Double rating;
        private Integer totalStudents;
        private Integer videoCount;

        // Constructors
        public CourseWithStatsDTO() {}

        // Getters and Setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public Integer getTotalStudents() {
            return totalStudents;
        }

        public void setTotalStudents(Integer totalStudents) {
            this.totalStudents = totalStudents;
        }

        public Integer getVideoCount() {
            return videoCount;
        }

        public void setVideoCount(Integer videoCount) {
            this.videoCount = videoCount;
        }
    }
}
