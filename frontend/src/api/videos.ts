import api from "./index"

export interface VideoDTO {
    id?: string
    videoId?: string
    fileName?: string
    filePath?: string
    courseId: string
    description?: string
    duration?: number
}

export const getVideosByCourse = async (courseId: string): Promise<VideoDTO[]> => {
    try {
        const response = await api.get(`/api/videos/courses/${courseId}`)
        return response.data
    } catch (error) {
        console.error("Error fetching videos:", error)
        throw error
    }
}

export const uploadVideo = async (courseId: string, file: File): Promise<VideoDTO> => {
    try {
        const formData = new FormData()
        formData.append("file", file)

        const response = await api.post(`/api/videos/${courseId}/upload`, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
        })
        return response.data
    } catch (error) {
        console.error("Error uploading video:", error)
        throw error
    }
}

export const updateVideo = async (videoId: string, videoData: Partial<VideoDTO>): Promise<VideoDTO> => {
    try {
        const response = await api.put(`/api/videos/${videoId}`, videoData)
        return response.data
    } catch (error) {
        console.error("Error updating video:", error)
        throw error
    }
}

export const deleteVideo = async (videoId: string): Promise<void> => {
    try {
        await api.delete(`/api/videos/${videoId}`)
    } catch (error) {
        console.error("Error deleting video:", error)
        throw error
    }
}
