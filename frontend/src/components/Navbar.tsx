"use client"

import { useContext } from "react"
import { Link, useNavigate } from "react-router-dom"
import { Navbar as BootstrapNavbar, Nav, Container, Button, NavDropdown } from "react-bootstrap"
import {UserContext} from "../context/UserContext";



const Navbar = () => {
    const { user, setUser } = useContext(UserContext)
    const navigate = useNavigate()

    const handleLogout = () => {
        setUser(null)
        navigate("/")
    }

    return (
        <BootstrapNavbar bg="light" expand="lg" className="shadow-sm">
            <Container>
                <BootstrapNavbar.Brand as={Link} to="/" className="fw-bold text-primary">
                    LearnHub
                </BootstrapNavbar.Brand>
                <BootstrapNavbar.Toggle aria-controls="basic-navbar-nav" />
                <BootstrapNavbar.Collapse id="basic-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/">
                            Home
                        </Nav.Link>
                        <Nav.Link as={Link} to="/courses">
                            Courses
                        </Nav.Link>
                    </Nav>
                    <Nav>
                        {user ? (
                            <NavDropdown
                                title={
                                    <span>
                    <img
                        src={user.avatar || "/placeholder.svg?height=30&width=30&query=user"}
                        alt="User"
                        className="rounded-circle me-2"
                        width="30"
                        height="30"
                    />
                                        {user.name}
                  </span>
                                }
                                id="basic-nav-dropdown"
                            >
                                <NavDropdown.Item as={Link} to={user.role === "student" ? "/profile/student" : "/profile/instructor"}>
                                    My Profile
                                </NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item onClick={handleLogout}>Logout</NavDropdown.Item>
                            </NavDropdown>
                        ) : (
                            <>
                                <Button as={Link} to="/login" variant="outline-primary" className="me-2">
                                    Login
                                </Button>
                                <Button as={Link} to="/register" variant="primary">
                                    Register
                                </Button>
                            </>
                        )}
                    </Nav>
                </BootstrapNavbar.Collapse>
            </Container>
        </BootstrapNavbar>
    )
}

export default Navbar
