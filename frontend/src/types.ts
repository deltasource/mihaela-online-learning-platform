export interface UserType {
    id: number
    name: string
    role: string
}

export interface Course {
    id: number
    title: string
    description: string
    thumbnail?: string
    price: number
    duration: string
    category: string
    rating: number
    reviewCount: number
    instructor: {
        name: string
        title?: string
        avatar?: string
        bio?: string
    }
    learningOutcomes?: string[]
    modules?: {
        title: string
        lessons: {
            title: string
            duration: string
            videoUrl: string
        }[]
    }[]
    lessons?: {
        title: string
        duration: string
        videoUrl: string
    }[]
    resources?: {
        title: string
        type: string
        url: string
    }[]
    reviews?: {
        name: string
        avatar?: string
        rating: number
        date: string
        comment: string
    }[]
}
