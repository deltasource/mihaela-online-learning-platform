import { BrowserRouter, Routes, Route } from "react-router-dom"
import { Suspense, lazy } from "react"
import Navbar from "./components/common/Navbar"
import Footer from "./components/common/Footer"
import HomePage from "./pages/HomePage"
import LoadingSpinner from "./components/common/LoadingSpinner"
import { ToastContainer } from "react-toastify"
import "react-toastify/dist/ReactToastify.css"
import "./App.css"
import CoursesPage from "./pages/courses/CoursePage";

const CourseDetailPage = lazy(() => import("./pages/courses/CourseDetailPage"))
const InstructorsPage = lazy(() => import("./pages/instructors/InstructorsPage"))
const StudentsPage = lazy(() => import("./pages/students/StudentsPage"))
const StudentProgressPage = lazy(() => import("./pages/students/StudentProgressPage"))
const VideoUploadPage = lazy(() => import("./pages/videos/VideoUploadPage"))
const NotFoundPage = lazy(() => import("./pages/NotFoundPage"))

function App() {
    return (
        <BrowserRouter>
            <div className="app-container">
                <Navbar />
                <main className="main-content">
                    <Suspense fallback={<LoadingSpinner />}>
                        <Routes>
                            <Route path="/" element={<HomePage />} />
                            <Route path="/courses" element={<CoursesPage />} />
                            <Route path="/courses/:id" element={<CourseDetailPage />} />
                            <Route path="/instructors" element={<InstructorsPage />} />
                            <Route path="/students" element={<StudentsPage />} />
                            <Route path="/students/:studentId/progress/:courseId" element={<StudentProgressPage />} />
                            <Route path="/videos/upload/:courseId" element={<VideoUploadPage />} />
                            <Route path="*" element={<NotFoundPage />} />
                        </Routes>
                    </Suspense>
                </main>
                <Footer />
                <ToastContainer position="bottom-right" />
            </div>
        </BrowserRouter>
    )
}

export default App
