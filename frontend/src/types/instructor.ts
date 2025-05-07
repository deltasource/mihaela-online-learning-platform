import type { UUID } from "./common"

export interface Instructor {
    id?: UUID
    email: string
    firstName: string
    lastName: string
    department: string
}
