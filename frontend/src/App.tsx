import { BrowserRouter, Routes, Route } from "react-router-dom"
import { Container } from "react-bootstrap"
import Navigation from "./components/Navigation"
import Home from "./pages/Home"
import Students from "./pages/Students"
import Instructors from "./pages/Instructors"
import Courses from "./pages/Courses"
import Videos from "./pages/Videos"
import StudentProgress from "./pages/StudentProgress"
import "bootstrap/dist/css/bootstrap.min.css"
import "./App.css"

function App() {
    return (
        <BrowserRouter>
            <Navigation />
            <Container className="py-4">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/students" element={<Students />} />
                    <Route path="/instructors" element={<Instructors />} />
                    <Route path="/courses" element={<Courses />} />
                    <Route path="/videos" element={<Videos />} />
                    <Route path="/progress" element={<StudentProgress />} />
                </Routes>
            </Container>
        </BrowserRouter>
    )
}

export default App
