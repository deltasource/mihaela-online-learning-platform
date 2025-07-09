# Online Learning Platform

The Online Learning Platform provides a solution for managing and delivering online education. Instructors can create
courses, upload videos, and track student progress, while students can enroll, access materials, and monitor their
learning. Built with a React.js frontend using Vite and a Spring Boot backend API, the platform offers a scalable and
secure environment for both educators and learners.

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
    git clone https://github.com/deltasource/mihaela-online-learning-platform.git
    cd online-learning-platform
    ```  

### Building and running your application

1. **Navigate to the project root directory**:
    ```bash
    cd online-learning-platform
    ```
2. **Navigate to the project backend directory:**

  ```bash
       cd backend
   ```

3. **For backend run we need to run this to create .jar file:**
   ```bash
     mvn clean install
   ```

4. **Navigate to the project frontend directory:**

 ```bash
      cd ../frontend
 ```

3. **For frontend run to install all needed dependencies:**

   ```bash
    npm install
   ```
4. **Navigate back to the project root directory**:
    ```bash
    cd ..
    ```

5. **When you're ready, start your application by running:**

 ```bash
 docker compose up --build
 ``` 

### Backend: Running the Spring Boot Application

The backend is built with Spring Boot and is responsible for handling business logic, managing databases, and providing
API endpoints.

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

The Spring Boot application will be accessible at http://localhost:8080 by default.

### Frontend: Running the React + Vite Application

The frontend is built with React.js and Vite, providing a user interface that interacts with the backend API.

1. **Install the required dependencies: Make sure you have Node.js installed on your machine. If not, download and
   install it from here. Then, run the following command to install the necessary packages**:
   Navigate to the `target` folder:
    ```bash
   npm install
    ```  
2. **Start the development server: After the dependencies are installed, you can run the frontend application**:

    ```bash
   npm run dev
    ```  

This will start the Vite development server. The frontend will be accessible at http://localhost:3000 by default.

## Accessing the Platform

Backend (API): http://localhost:8080

Frontend (UI): http://localhost:3000

### OpenAPI Documentation

The OpenAPI documentation for the Online Learning Platform API is available in the backend/videos folder of the project.
You can access the documentation directly by opening the api-docs.json file located there.

1.**Navigate to**:

http://localhost:8080/swagger-ui/index.html#

2. **Open the api-docs.json file in any text editor to explore the complete API documentation.**

Alternatively, you can use tools like Swagger UI or Postman to visualize and interact with the API documentation in a
more user-friendly interface.

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
