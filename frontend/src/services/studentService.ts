import api from "./api"
import { Student } from "../types"

const STUDENTS_URL = "/students/v1"

export const getStudents = async (): Promise<Student[]> => {
    const response = await api.get<Student[]>(STUDENTS_URL)
    return response.data
}

export const getStudentByEmail = async (email: string): Promise<Student> => {
    const response = await api.get<Student>(`${STUDENTS_URL}/${email}`)
    return response.data
}

export const createStudent = async (studentData: Omit<Student, "id">): Promise<Student> => {
    const response = await api.post<Student>(STUDENTS_URL, studentData)
    return response.data
}

export const updateStudent = async (email: string, studentData: Partial<Student>): Promise<Student> => {
    const response = await api.put<Student>(`${STUDENTS_URL}/${email}`, studentData)
    return response.data
}

export const deleteStudent = async (email: string): Promise<void> => {
    await api.delete(`${STUDENTS_URL}/${email}`)
}
