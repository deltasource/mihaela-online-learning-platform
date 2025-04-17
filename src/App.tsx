import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import InstructorProfile from "./pages/InstructorProfile";
import StudentProfile from "./pages/StudentProfile";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/instructor" element={<InstructorProfile />} />
                <Route path="/student" element={<StudentProfile />} />
            </Routes>
        </Router>
    );
}

export default App;
