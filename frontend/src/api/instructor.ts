import api from "./index"

export interface InstructorDTO {
    id?: string
    firstName: string
    lastName: string
    email: string
    department: string
}

export const getAllInstructors = async (): Promise<InstructorDTO[]> => {
    try {
        const response = await api.get("/instructors/v1")
        return response.data
    } catch (error) {
        console.error("Error fetching instructors:", error)
        throw error
    }
}

export const getInstructorByEmail = async (email: string): Promise<InstructorDTO> => {
    try {
        const response = await api.get(`/instructors/v1/${email}`)
        return response.data
    } catch (error) {
        console.error("Error fetching instructor:", error)
        throw error
    }
}

export const createInstructor = async (instructorData: Omit<InstructorDTO, "id">): Promise<InstructorDTO> => {
    try {
        const response = await api.post("/instructors/v1", instructorData)
        return response.data
    } catch (error) {
        console.error("Error creating instructor:", error)
        throw error
    }
}

export const updateInstructor = async (
    email: string,
    instructorData: Partial<InstructorDTO>,
): Promise<InstructorDTO> => {
    try {
        const response = await api.put(`/instructors/v1/${email}`, instructorData)
        return response.data
    } catch (error) {
        console.error("Error updating instructor:", error)
        throw error
    }
}

export const deleteInstructor = async (email: string): Promise<void> => {
    try {
        await api.delete(`/instructors/v1/${email}`)
    } catch (error) {
        console.error("Error deleting instructor:", error)
        throw error
    }
}
