export interface Course {
    title: string
    description: string
    progress: number
}

export interface TeachingSchedule {
    courseId: number
    courseTitle: string
    startDate: string
    endDate: string
}

export interface SocialLinks {
    linkedin?: string
    instagram?: string
    github?: string
    twitter?: string
}

export interface Instructor {
    id: number
    name: string
    role: string
    title: string
    bio: string
    avatar: string
    coverImage: string
    socialLinks: SocialLinks
    courses: Course[]
    teaching: TeachingSchedule[]
}

export interface User {
    id: number
    name: string
    role: string
    title: string
    bio: string
    avatar: string
    coverImage: string
    socialLinks: SocialLinks
    courses: Course[]
    instructor: Instructor
}

export const mockUserData: User = {
    id: 1,
    name: "Mihaela",
    role: "Student",
    title: "Trainee Full Stack",
    bio: "Ready to learn.",
    avatar: "",
    coverImage: "",
    socialLinks: {
        linkedin: "https://www.linkedin.com/in/mihaela-kolarova-380146231/",
        instagram: "https://www.instagram.com/mihaela003",
        github: "https://github.com/mihaelakolarova03",
    },
    courses: [
        {
            title: "Web Development Bootcamp",
            description: "Learn web development from scratch.",
            progress: 75,
        },
        {
            title: "React Masterclass",
            description: "Become a React expert.",
            progress: 40,
        },
        {
            title: "Node.js API Development",
            description: "Build scalable APIs with Node.js.",
            progress: 90,
        },
    ],
    instructor: {
        id: 2,
        name: "Eray",
        role: "Instructor",
        title: "Full Stack Developer",
        bio: "Passionate about teaching and coding.",
        avatar: "",
        coverImage: "",
        socialLinks: {
            linkedin: "https://www.linkedin.com/in/erayali/",
            twitter: "",
            github: "",
        },
        courses: [
            {
                title: "Advanced React",
                description: "Dive deep into React and build production-ready apps.",
                progress: 100,
            },
            {
                title: "Java for Backend Developers",
                description: "Master Node.js and build scalable backends.",
                progress: 85,
            },
            {
                title: "Docker and Kubernetes Essentials",
                description: "Learn the basics of containerization and orchestration.",
                progress: 70,
            },
        ],
        teaching: [
            {
                courseId: 1,
                courseTitle: "Web Development Bootcamp",
                startDate: "2024-01-15",
                endDate: "2024-06-30",
            },
            {
                courseId: 2,
                courseTitle: "Advanced React",
                startDate: "2024-02-01",
                endDate: "2024-05-30",
            },
        ],
    },
}
