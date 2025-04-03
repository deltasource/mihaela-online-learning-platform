# Online Learning Platform API  

This repository contains the Online Learning Platform API, which provides endpoints for managing students, instructors, courses, and videos.  

## Features  

- Manage students and instructors.  
- Upload and manage course videos.  
- Handle course enrollments and details.  

## Getting Started  

### Prerequisites  

- [Node.js](https://nodejs.org/)  
- [Java 21](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)  
- [Maven](https://maven.apache.org/download.cgi)  

### Installation  

1. Clone the repository:  
    ```bash
    git clone [https://github.com/your-repo/online-learning-platform-api.git](https://github.com/deltasource/mihaela-online-learning-platform.git)
    cd online-learning-platform
    ```  

### How to Run the Program  

There are two ways to run the application:  

#### Option 1: Using Maven Commands  
1. **Run the Maven build**:  
    ```bash
    mvn clean install
    ```  
   This will compile the code and package the application into a `.jar` file.  

2. **Run the packaged application**:  
   Navigate to the `target` folder:  
    ```bash
    cd target
    ```  
   Run the JAR file:  
    ```bash
    java -jar *.jar
    ```  
   The application should now be running and accessible.  

#### Option 2: Using IntelliJ IDEA with Maven Delegation  
1. Open IntelliJ IDEA.  
2. Go to `File` > `Settings` > `Build, Execution, Deployment` > `Build Tools` > `Maven` > `Runner`.  
3. Enable the option **"Delegate IDE build/run actions to Maven"**.  
4. Run the application directly using the IntelliJ IDEA run configuration.  

The application should now be running and accessible.  

## API Endpoints  

Here are some of the key endpoints provided by the API:  

### Student Management  
- `POST /students/v1`: Create a new student.  
- `GET /students/v1/{email}`: Retrieve a student by email.  
- `PUT /students/v1/{email}`: Update a student's details.  
- `DELETE /students/v1/{email}`: Delete a student.  

### Instructor Management  
- `POST /instructors/v1`: Create a new instructor.  
- `GET /instructors/v1/{email}`: Retrieve an instructor by email.  
- `PUT /instructors/v1/{email}`: Update an instructor's details.  
- `DELETE /instructors/v1/{email}`: Delete an instructor.  

### Course Management  
- `POST /api/courses`: Create a new course.  
- `GET /api/courses`: Retrieve all courses.  
- `GET /api/courses/{courseId}`: Retrieve a course by ID.  
- `PUT /api/courses/{courseId}`: Update a course.  
- `DELETE /api/courses/{courseId}`: Delete a course.  

### Video Management  
- `POST /api/videos/{courseId}/upload`: Upload a video for a course.  
- `GET /api/videos/courses/{courseId}`: Retrieve all videos for a course.  
- `PUT /api/videos/{videoId}`: Update a video's metadata.  
- `DELETE /api/videos/{videoId}`: Delete a video.  

For a full list of endpoints, please refer to the OpenAPI documentation.  
