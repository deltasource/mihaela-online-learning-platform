import apiClient from "./client"
import type { AuthResponse, LoginRequest, RegisterRequest, User } from "../types/auth"


const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const authService = {
  async login(credentials: LoginRequest): Promise<AuthResponse> {
    try {
      const response = await apiClient.post<AuthResponse>(`${BASE_URL}/api/auth/login`, credentials)

      return response.data
    } catch (error) {
      console.error("Login error:", error)
      throw error
    }
  },

  async register(userData: RegisterRequest): Promise<AuthResponse> {
    try {
console.log("TYK SYM1")
      const response = await apiClient.post<AuthResponse>(`${BASE_URL}/api/auth/register`, userData)
      console.log("TYK SYM")
      console.log(response);
      return response.data
    } catch (error) {
      console.error("Register error:", error)
      throw error
    }
  },

  async updateProfile(userId: string, userData: Partial<User>): Promise<User> {
    try {
      const response = await apiClient.put<User>(`${BASE_URL}/api/users/${userId}`, userData)

      return response.data
    } catch (error) {
      console.error("Update profile error:", error)
      throw error
    }
  },

  async getCurrentUser(): Promise<User | null> {
    try {
      const token = localStorage.getItem("authToken")

      if (!token) {
        return null
      }

      const response = await apiClient.get<User>(`${BASE_URL}/api/auth/me`)

      return response.data
    } catch (error) {
      console.error("Get current user error:", error)
      return null
    }
  },

  logout(): void {
  },
}
