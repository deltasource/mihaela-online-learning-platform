import { Link, useLocation } from "react-router-dom"
import { Navbar as BsNavbar, Nav, Container } from "react-bootstrap"

const Navbar = () => {
    const location = useLocation()

    const isActive = (path: string) => {
        return location.pathname === path ? "active" : ""
    }

    return (
        <BsNavbar bg="light" expand="lg" className="shadow-sm mb-4">
            <Container>
                <BsNavbar.Brand as={Link} to="/" className="fw-bold">
                    E-Learning Platform
                </BsNavbar.Brand>
                <BsNavbar.Toggle aria-controls="basic-navbar-nav" />
                <BsNavbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/" className={isActive("/")}>
                            Home
                        </Nav.Link>
                        <Nav.Link as={Link} to="/courses" className={isActive("/courses")}>
                            Courses
                        </Nav.Link>
                        <Nav.Link as={Link} to="/instructors" className={isActive("/instructors")}>
                            Instructors
                        </Nav.Link>
                        <Nav.Link as={Link} to="/students" className={isActive("/students")}>
                            Students
                        </Nav.Link>
                    </Nav>
                </BsNavbar.Collapse>
            </Container>
        </BsNavbar>
    )
}

export default Navbar
