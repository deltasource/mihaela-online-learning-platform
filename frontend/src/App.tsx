"use client"

import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import { useState } from "react"
import Header from "./components/Header"
import Footer from "./components/Footer"
import HomePage from "./components/HomePage"
import CoursePage from "./components/CoursePage"
import CourseDetail from "./components/CourseDetail"
import Dashboard from "./components/Dashboard"
import { ProfilePage, InstructorPage } from "./components/profile"
import "bootstrap/dist/css/bootstrap.min.css"
import "./App.css"

function App() {
    const [currentPage, setCurrentPage] = useState<string>("home")

    const handleBackClick = () => {
        setCurrentPage("home")
    }

    return (
        <Router>
            <div className="app-container d-flex flex-column min-vh-100">
                {currentPage === "profile" ? (
                    <ProfilePage onBackClick={handleBackClick} />
                ) : currentPage === "instructor" ? (
                    <InstructorPage onBackClick={handleBackClick} />
                ) : (
                    <>
                        <Header
                            onProfileClick={() => setCurrentPage("profile")}
                            onInstructorClick={() => setCurrentPage("instructor")}
                        />
                        <main className="flex-grow-1">
                            <Routes>
                                <Route path="/" element={<HomePage />} />
                                <Route path="/courses" element={<CoursePage />} />
                                <Route path="/course/:id" element={<CourseDetail />} />
                                <Route path="/dashboard" element={<Dashboard />} />
                            </Routes>
                        </main>
                        <Footer />
                    </>
                )}
            </div>
        </Router>
    )
}

export default App
