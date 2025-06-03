"use client"

import { createContext, useContext, useEffect, useState, type ReactNode } from "react"
import type { User } from "../types"
import { getCurrentUser, login, logout, register } from "../api/auth"
import { jwtDecode } from "jwt-decode"

interface AuthContextType {
  user: User | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (email: string, password: string) => Promise<void>
  register: (name: string, email: string, password: string, role: "student" | "instructor") => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const initAuth = async () => {
      const token = localStorage.getItem("token")

      if (token) {
        try {
          // Check if token is expired (only for real JWT tokens, not mock ones)
          if (!token.startsWith("mock-token-")) {
            const decodedToken = jwtDecode(token)
            const currentTime = Date.now() / 1000

            if (decodedToken.exp && decodedToken.exp < currentTime) {
              // Token expired
              logout()
              setUser(null)
              setIsLoading(false)
              return
            }
          }

          // Token valid, get current user
          const userData = await getCurrentUser()
          setUser(userData)
        } catch (error) {
          console.error("Failed to initialize auth:", error)
          logout()
          setUser(null)
        }
      }

      setIsLoading(false)
    }

    initAuth()
  }, [])

  const handleLogin = async (email: string, password: string) => {
    setIsLoading(true)
    try {
      const response = await login(email, password)
      setUser(response.user)
    } catch (error) {
      console.error("Login failed:", error)
      throw error
    } finally {
      setIsLoading(false)
    }
  }

  const handleRegister = async (name: string, email: string, password: string, role: "student" | "instructor") => {
    setIsLoading(true)
    try {
      const response = await register(name, email, password, role)
      setUser(response.user)
    } catch (error) {
      console.error("Registration failed:", error)
      throw error
    } finally {
      setIsLoading(false)
    }
  }

  const handleLogout = () => {
    logout()
    setUser(null)
  }

  return (
      <AuthContext.Provider
          value={{
            user,
            isAuthenticated: !!user,
            isLoading,
            login: handleLogin,
            register: handleRegister,
            logout: handleLogout,
          }}
      >
        {children}
      </AuthContext.Provider>
  )
}

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}
