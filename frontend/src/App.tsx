"use client"

import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import { useState } from "react"
import Navbar from "./components/Navbar"
import Footer from "./components/Footer"
import HomePage from "./pages/HomePage"
import CoursesPage from "./pages/CoursesPage"
import CourseDetailPage from "./pages/CourseDetailPage"
import StudentProfilePage from "./pages/StudentProfilePage"
import InstructorProfilePage from "./pages/InstructorProfilePage"
import LoginPage from "./pages/LoginPage"
import RegisterPage from "./pages/RegisterPage"
import "bootstrap/dist/css/bootstrap.min.css"
import "./App.css"
import { UserContext } from "./context/UserContext"
import {UserType} from "./types";


function App() {
    const [user, setUser] = useState<UserType | null>(null)

    return (
        <UserContext.Provider value={{ user, setUser }}>
            <Router>
                <div className="d-flex flex-column min-vh-100">
                    <Navbar />
                    <main className="flex-grow-1">
                        <Routes>
                            <Route path="/" element={<HomePage />} />
                            <Route path="/courses" element={<CoursesPage />} />
                            <Route path="/courses/:id" element={<CourseDetailPage />} />
                            <Route path="/profile/student" element={<StudentProfilePage />} />
                            <Route path="/profile/instructor" element={<InstructorProfilePage />} />
                            <Route path="/login" element={<LoginPage />} />
                            <Route path="/register" element={<RegisterPage />} />
                        </Routes>
                    </main>
                    <Footer />
                </div>
            </Router>
        </UserContext.Provider>
    )
}

export default App
