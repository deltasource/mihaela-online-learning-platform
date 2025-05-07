import type { UUID } from "./common"

export interface Student {
    id?: UUID
    email: string
    firstName: string
    lastName: string
}
