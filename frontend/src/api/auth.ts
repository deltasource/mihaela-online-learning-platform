import api from "./index"
import type { AuthResponse, LoginRequest, RegisterRequest, User } from "../types"

export const authAPI = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post("/auth/login", credentials)
    return response.data
  },

  register: async (userData: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post("/auth/register", userData)
    return response.data
  },

  logout: () => {
    localStorage.removeItem("token")
    localStorage.removeItem("user")
  },

  getCurrentUser: async (): Promise<User> => {
    const response = await api.get("/auth/me")
    return response.data
  },
}

// Mock authentication for testing
export const mockAuth = {
  login: async (email: string, password: string): Promise<AuthResponse> => {
    const mockUsers = {
      "eray.ali@example.com": {
        id: "123e4567-e89b-12d3-a456-426614174000",
        name: "Eray Ali",
        email: "eray.ali@example.com",
        role: "instructor" as const,
      },
      "mihaela.kolarova@example.com": {
        id: "123e4567-e89b-12d3-a456-426614174001",
        name: "Mihaela Kolarova",
        email: "mihaela.kolarova@example.com",
        role: "student" as const,
      },
    }

    if (mockUsers[email]) {
      const user = mockUsers[email]
      const token = `mock-token-${Date.now()}`

      localStorage.setItem("user", JSON.stringify(user))
      localStorage.setItem("token", token)

      return { user, token }
    }

    throw new Error("Invalid email or password")
  },

  register: async (
      name: string,
      email: string,
      password: string,
      role: "student" | "instructor",
  ): Promise<AuthResponse> => {
    const [firstName, ...lastNameParts] = name.split(" ")
    const lastName = lastNameParts.join(" ") || ""

    const user = {
      id: `mock-${Date.now()}`,
      name,
      email,
      role: role as "student" | "instructor",
    }

    const token = `mock-token-${Date.now()}`

    localStorage.setItem("user", JSON.stringify(user))
    localStorage.setItem("token", token)

    return { user, token }
  },
}

export const login = async (email: string, password: string): Promise<AuthResponse> => {
  try {
    return await authAPI.login({ email, password })
  } catch (error) {
    console.error("JWT Login failed, trying mock auth:", error)
    return await mockAuth.login(email, password)
  }
}

export const register = async (
    name: string,
    email: string,
    password: string,
    role: "student" | "instructor",
): Promise<AuthResponse> => {
  try {
    const [firstName, ...lastNameParts] = name.split(" ")
    const lastName = lastNameParts.join(" ") || ""

    const registerData: RegisterRequest = {
      firstName,
      lastName,
      email,
      password,
      role: role.toUpperCase() as "STUDENT" | "INSTRUCTOR",
    }

    return await authAPI.register(registerData)
  } catch (error) {
    console.error("JWT Registration failed, trying mock auth:", error)
    // Fall back to mock registration
    return await mockAuth.register(name, email, password, role)
  }
}

export const getCurrentUser = async (): Promise<User> => {
  try {
    return await authAPI.getCurrentUser()
  } catch (error) {
    const userStr = localStorage.getItem("user")
    if (userStr) {
      return JSON.parse(userStr)
    }
    throw new Error("No user found")
  }
}

export const logout = (): void => {
  authAPI.logout()
}
