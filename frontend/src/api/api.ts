const API_BASE_URL = "http://localhost:8080"

async function apiRequest<T>(endpoint: string, method = "GET", data?: any, isFormData = false): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`

    const headers: HeadersInit = {}

    if (!isFormData) {
        headers["Content-Type"] = "application/json"
    }

    const options: RequestInit = {
        method,
        headers,
        credentials: "include",
    }

    if (data) {
        if (isFormData) {
            options.body = data
        } else {
            options.body = JSON.stringify(data)
        }
    }

    const response = await fetch(url, options)

    if (response.status === 204) {
        return {} as T
    }

    if (!response.ok) {
        const errorData = await response.json().catch(() => ({}))
        throw new Error(errorData.message || `API request failed with status ${response.status}`)
    }

    if (response.headers.get("content-type")?.includes("application/json")) {
        return await response.json()
    }

    return {} as T
}

// Student API
export interface StudentDTO {
    email: string
    firstName: string
    lastName: string
}

export const studentApi = {
    createStudent: (student: StudentDTO) => apiRequest<StudentDTO>("/students/v1", "POST", student),

    getStudentByEmail: (email: string) => apiRequest<StudentDTO>(`/students/v1/${email}`),

    updateStudent: (email: string, student: StudentDTO) =>
        apiRequest<StudentDTO>(`/students/v1/${email}`, "PUT", student),

    deleteStudent: (email: string) => apiRequest<void>(`/students/v1/${email}`, "DELETE"),
}

// Instructor API
export interface InstructorDTO {
    email: string
    firstName: string
    lastName: string
    department: string
}

export const instructorApi = {
    createInstructor: (instructor: InstructorDTO) => apiRequest<InstructorDTO>("/instructors/v1", "POST", instructor),

    getInstructorByEmail: (email: string) => apiRequest<InstructorDTO>(`/instructors/v1/${email}`),

    updateInstructor: (email: string, instructor: InstructorDTO) =>
        apiRequest<InstructorDTO>(`/instructors/v1/${email}`, "PUT", instructor),

    deleteInstructor: (email: string) => apiRequest<void>(`/instructors/v1/${email}`, "DELETE"),
}

// Course API
export interface CourseDTO {
    name: string
    description: string
    instructorId?: string
    studentIds?: string[]
}

export const courseApi = {
    createCourse: (course: CourseDTO) => apiRequest<CourseDTO>("/api/courses", "POST", course),

    getAllCourses: () => apiRequest<CourseDTO[]>("/api/courses"),

    getCourseById: (courseId: string) => apiRequest<CourseDTO>(`/api/courses/${courseId}`),

    updateCourse: (courseId: string, course: CourseDTO) =>
        apiRequest<CourseDTO>(`/api/courses/${courseId}`, "PUT", course),

    deleteCourse: (courseId: string) => apiRequest<void>(`/api/courses/${courseId}`, "DELETE"),
}

// Video API
export interface VideoDTO {
    fileName?: string
    filePath?: string
    courseId: string
}

export const videoApi = {
    uploadVideo: (courseId: string, file: File) => {
        const formData = new FormData()
        formData.append("file", file)
        return apiRequest<VideoDTO>(`/api/videos/${courseId}/upload`, "POST", formData, true)
    },

    getVideosByCourse: (courseId: string) => apiRequest<VideoDTO[]>(`/api/videos/courses/${courseId}`),

    updateVideo: (videoId: string, video: VideoDTO) => apiRequest<VideoDTO>(`/api/videos/${videoId}`, "PUT", video),

    deleteVideo: (videoId: string) => apiRequest<void>(`/api/videos/${videoId}`, "DELETE"),
}

// Student Progress API
export interface StudentProgressDTO {
    studentId: string
    courseId: string
    progressPercentage: number
    totalVideos: number
    videosWatched: number
}

export const progressApi = {
    getProgressPercentage: (studentId: string, courseId: string) =>
        apiRequest<StudentProgressDTO>(`/students/progress/v1/${studentId}/courses/${courseId}`),

    updateProgress: (studentId: string, courseId: string, videoId: string) =>
        apiRequest<void>(`/students/progress/v1/${studentId}/courses/${courseId}/videos/${videoId}/update`, "POST"),
}
