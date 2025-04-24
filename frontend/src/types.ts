export interface Course {
    id: string
    name: string
    description: string
    instructorEmail: string
    duration: number
}

export interface Instructor {
    id: string
    name: string
    email: string
    bio: string
}

export interface Student {
    id: string
    name: string
    email: string
    courses: string[]
}

export interface Video {
    id: string
    courseId: string
    title: string
    videoUrl: string
}

export interface StudentProgress {
    studentId: string
    courseId: string
    videosWatched: string[]
}
