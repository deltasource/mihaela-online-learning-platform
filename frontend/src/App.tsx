"use client"

import type React from "react"
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom"
import { AuthProvider, useAuth } from "./context/AuthContext"

import HomePage from "./pages/HomePage"
import LoginPage from "./pages/auth/LoginPage"
import RegisterPage from "./pages/auth/RegisterPage"
import CoursesPage from "./pages/courses/CoursesPage"
import CourseDetailPage from "./pages/courses/CourseDetailPage"
import StudentDashboard from "./pages/dashboard/StudentDashboard"
import InstructorDashboard from "./pages/dashboard/InstructorDashboard"
import CreateCoursePage from "./components/instructor/CreateCoursePage.tsx";
import EditCoursePage from "./components/instructor/EditCoursePage.tsx";

const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated, isLoading } = useAuth()

  if (isLoading) {
    return (
        <div className="flex justify-center items-center min-h-screen">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
        </div>
    )
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" />
  }

  return <>{children}</>
}

const InstructorRoute = ({ children }: { children: React.ReactNode }) => {
  const { user, isLoading } = useAuth()

  if (isLoading) {
    return (
        <div className="flex justify-center items-center min-h-screen">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
        </div>
    )
  }

  if (!user || user.role !== "instructor") {
    return <Navigate to="/dashboard" />
  }

  return <>{children}</>
}

const App: React.FC = () => {
  return (
      <Router>
        <AuthProvider>
          <Routes>
            {/* Public routes */}
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/courses" element={<CoursesPage />} />
            <Route path="/courses/:id" element={<CourseDetailPage />} />

            {/* Protected routes */}
            <Route
                path="/dashboard"
                element={
                  <ProtectedRoute>
                    <StudentDashboard />
                  </ProtectedRoute>
                }
            />

            {/* Instructor routes */}
            <Route
                path="/instructor/dashboard"
                element={
                  <InstructorRoute>
                    <InstructorDashboard />
                  </InstructorRoute>
                }
            />
            <Route
                path="/instructor/courses/new"
                element={
                  <InstructorRoute>
                    <CreateCoursePage />
                  </InstructorRoute>
                }
            />
            <Route
                path="/instructor/courses/:id/edit"
                element={
                  <InstructorRoute>
                    <EditCoursePage />
                  </InstructorRoute>
                }
            />

            {/* Fallback route */}
            <Route path="*" element={<Navigate to="/" />} />
          </Routes>
        </AuthProvider>
      </Router>
  )
}

export default App
