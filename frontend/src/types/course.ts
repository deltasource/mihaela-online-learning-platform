import type { UUID } from "./common";

export interface Course {
    id?: UUID
    name: string
    description: string
    instructorId: UUID
    studentIds?: UUID[]
}
