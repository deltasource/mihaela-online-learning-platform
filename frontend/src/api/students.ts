import api from "./index"

export interface StudentDTO {
    id?: string
    email: string
    firstName: string
    lastName: string
}

export interface StudentProgressDTO {
    studentId: string
    courseId: string
    progressPercentage: number
    totalVideos: number
    videosWatched: number
}

export const createStudent = async (studentData: StudentDTO): Promise<StudentDTO> => {
    try {
        const response = await api.post("/students/v1", studentData)
        return response.data
    } catch (error) {
        console.error("Error creating student:", error)
        throw error
    }
}

export const getStudentByEmail = async (email: string): Promise<StudentDTO> => {
    try {
        const response = await api.get(`/students/v1/${email}`)
        return response.data
    } catch (error) {
        console.error("Error fetching student:", error)
        throw error
    }
}

export const updateStudent = async (email: string, studentData: Partial<StudentDTO>): Promise<StudentDTO> => {
    try {
        const response = await api.put(`/students/v1/${email}`, studentData)
        return response.data
    } catch (error) {
        console.error("Error updating student:", error)
        throw error
    }
}

export const deleteStudent = async (email: string): Promise<void> => {
    try {
        await api.delete(`/students/v1/${email}`)
    } catch (error) {
        console.error("Error deleting student:", error)
        throw error
    }
}

export const getStudentProgress = async (studentId: string, courseId: string): Promise<StudentProgressDTO> => {
    try {
        const response = await api.get(`/students/progress/v1/${studentId}/courses/${courseId}`)
        return response.data
    } catch (error) {
        console.error("Error fetching student progress:", error)
        throw error
    }
}

export const updateStudentProgress = async (studentId: string, courseId: string, videoId: string): Promise<void> => {
    try {
        await api.post(`/students/progress/v1/${studentId}/courses/${courseId}/videos/${videoId}/update`)
    } catch (error) {
        console.error("Error updating student progress:", error)
        throw error
    }
}
