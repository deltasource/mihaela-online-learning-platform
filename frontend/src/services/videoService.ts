import api from "./api"
import { Video } from "../types"

const VIDEOS_URL = "/api/videos"

export const getVideosByCourse = async (courseId: string): Promise<Video[]> => {
    const response = await api.get<Video[]>(`${VIDEOS_URL}/courses/${courseId}`)
    return response.data
}

export const getVideoById = async (videoId: string): Promise<Video> => {
    const response = await api.get<Video>(`${VIDEOS_URL}/${videoId}`)
    return response.data
}

export const uploadVideo = async (courseId: string, videoFile: File): Promise<Video> => {
    const formData = new FormData()
    formData.append("file", videoFile)

    const response = await api.post<Video>(`${VIDEOS_URL}/${courseId}/upload`, formData, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
    })
    return response.data
}

export const updateVideo = async (videoId: string, videoData: Partial<Video>): Promise<Video> => {
    const response = await api.put<Video>(`${VIDEOS_URL}/${videoId}`, videoData)
    return response.data
}

export const deleteVideo = async (videoId: string): Promise<void> => {
    await api.delete(`${VIDEOS_URL}/${videoId}`)
}
