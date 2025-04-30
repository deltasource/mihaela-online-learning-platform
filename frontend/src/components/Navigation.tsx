import { Navbar, Nav, Container } from "react-bootstrap"
import { Link } from "react-router-dom"

const Navigation = () => {
    return (
        <Navbar bg="dark" variant="dark" expand="lg">
            <Container>
                <Navbar.Brand as={Link} to="/">
                    E-Learning Platform
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/">
                            Home
                        </Nav.Link>
                        <Nav.Link as={Link} to="/students">
                            Students
                        </Nav.Link>
                        <Nav.Link as={Link} to="/instructors">
                            Instructors
                        </Nav.Link>
                        <Nav.Link as={Link} to="/courses">
                            Courses
                        </Nav.Link>
                        <Nav.Link as={Link} to="/videos">
                            Videos
                        </Nav.Link>
                        <Nav.Link as={Link} to="/progress">
                            Progress
                        </Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    )
}

export default Navigation
