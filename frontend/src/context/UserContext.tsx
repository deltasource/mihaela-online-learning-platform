import { createContext, type Dispatch, type SetStateAction } from "react"

export type UserType = {
    id: string
    name: string
    email: string
    role: "student" | "instructor"
    avatar?: string
}

type UserContextType = {
    user: UserType | null
    setUser: Dispatch<SetStateAction<UserType | null>>
}

export const UserContext = createContext<UserContextType>({
    user: null,
    setUser: () => {},
})
