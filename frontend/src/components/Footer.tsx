import { Container, Row, Col } from "react-bootstrap"
import { Link } from "react-router-dom"

const Footer = () => {
    return (
        <footer className="bg-dark text-light py-4 mt-5">
            <Container>
                <Row>
                    <Col md={4} className="mb-3 mb-md-0">
                        <h5 className="mb-3">LearnHub</h5>
                        <p className="text-muted">Empowering learners worldwide with accessible, quality education.</p>
                    </Col>
                    <Col md={2} className="mb-3 mb-md-0">
                        <h5 className="mb-3">Links</h5>
                        <ul className="list-unstyled">
                            <li>
                                <Link to="/" className="text-decoration-none text-muted">
                                    Home
                                </Link>
                            </li>
                            <li>
                                <Link to="/courses" className="text-decoration-none text-muted">
                                    Courses
                                </Link>
                            </li>
                            <li>
                                <Link to="/login" className="text-decoration-none text-muted">
                                    Login
                                </Link>
                            </li>
                            <li>
                                <Link to="/register" className="text-decoration-none text-muted">
                                    Register
                                </Link>
                            </li>
                        </ul>
                    </Col>
                    <Col md={3} className="mb-3 mb-md-0">
                        <h5 className="mb-3">Resources</h5>
                        <ul className="list-unstyled">
                            <li>
                                <a href="#" className="text-decoration-none text-muted">
                                    Blog
                                </a>
                            </li>
                            <li>
                                <a href="#" className="text-decoration-none text-muted">
                                    Help Center
                                </a>
                            </li>
                            <li>
                                <a href="#" className="text-decoration-none text-muted">
                                    Tutorials
                                </a>
                            </li>
                            <li>
                                <a href="#" className="text-decoration-none text-muted">
                                    FAQ
                                </a>
                            </li>
                        </ul>
                    </Col>
                    <Col md={3}>
                        <h5 className="mb-3">Contact</h5>
                        <ul className="list-unstyled text-muted">
                            <li>Email: contact@learnhub.com</li>
                            <li>Phone: +1 (123) 456-7890</li>
                            <li>Address: 123 Education St, Learning City</li>
                        </ul>
                    </Col>
                </Row>
                <hr className="my-3 bg-secondary" />
                <div className="text-center text-muted">
                    <small>&copy; {new Date().getFullYear()} LearnHub. All rights reserved.</small>
                </div>
            </Container>
        </footer>
    )
}

export default Footer
