import api from "./api"
import {Course, Video} from "../types"

const COURSES_URL = "/api/courses"
const VIDEOS_URL = "/api/videos"

export const getCourses = async (): Promise<Course[]> => {
    const response = await api.get<Course[]>(COURSES_URL)
    return response.data
}

export const getCourseById = async (id: string): Promise<Course> => {
    const response = await api.get<Course>(`${COURSES_URL}/${id}`)
    return response.data
}

export const getCoursesByInstructor = async (instructorEmail: string): Promise<Course[]> => {
    const response = await api.get<Course[]>(`${COURSES_URL}?instructorEmail=${instructorEmail}`)
    return response.data
}

export const createCourse = async (courseData: Omit<Course, "id">): Promise<Course> => {
    const response = await api.post<Course>(COURSES_URL, courseData)
    return response.data
}

export const updateCourse = async (id: string, courseData: Partial<Course>): Promise<Course> => {
    const response = await api.put<Course>(`${COURSES_URL}/${id}`, courseData)
    return response.data
}

export const deleteCourse = async (id: string): Promise<void> => {
    await api.delete(`${COURSES_URL}/${id}`)
}

export const getVideosByCourse = async (courseId: string): Promise<Video[]> => {
    const response = await api.get<Video[]>(`${VIDEOS_URL}/courses/${courseId}`)
    return response.data
}
