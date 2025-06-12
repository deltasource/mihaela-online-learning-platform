export interface User {
    id: string
    name: string
    email: string
    role: "student" | "instructor"
    avatar?: string
    firstName?: string
    lastName?: string
}

export interface AuthResponse {
    user: User
    token: string
}

export interface LoginRequest {
    email: string
    password: string
}

export interface RegisterRequest {
    firstName: string
    lastName: string
    email: string
    password: string
    role: "STUDENT" | "INSTRUCTOR"
}

export interface Course {
    id: string
    title: string
    description: string
    thumbnail: string
    instructorId: string
    instructorName: string
    category: string
    level: "beginner" | "intermediate" | "advanced"
    price: number
    rating: number
    totalStudents: number
    createdAt: string
    updatedAt: string
    studentIds?: string[]
    name?: string // For backend compatibility
}

export interface CourseDetails extends Course {
    sections: Section[]
}

export interface Section {
    id: string
    title: string
    courseId: string
    order: number
    lessons: Lesson[]
}

export interface Lesson {
    id: string
    title: string
    description: string
    videoUrl: string
    duration: number // in seconds
    sectionId: string
    order: number
    isPreview: boolean
}

export interface Enrollment {
    id: string
    userId: string
    courseId: string
    enrolledAt: string
    progress: number // percentage
    completedLessons: string[] // array of lesson IDs
}
