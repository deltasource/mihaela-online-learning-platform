import api from "./api"
import { StudentProgress } from "../types"

const PROGRESS_URL = "/students/progress/v1"

export const getStudentProgress = async (studentId: string, courseId: string): Promise<StudentProgress> => {
    const response = await api.get<StudentProgress>(`${PROGRESS_URL}/${studentId}/courses/${courseId}`)
    return response.data
}

export const updateStudentProgress = async (studentId: string, courseId: string, videoId: string): Promise<void> => {
    await api.post(`${PROGRESS_URL}/${studentId}/courses/${courseId}/videos/${videoId}/update`)
}

export const createStudentProgress = async (progressData: StudentProgress): Promise<StudentProgress> => {
    const response = await api.post<StudentProgress>(PROGRESS_URL, progressData)
    return response.data
}

export const deleteStudentProgress = async (studentId: string, courseId: string): Promise<void> => {
    await api.delete(`${PROGRESS_URL}/${studentId}/courses/${courseId}`)
}
