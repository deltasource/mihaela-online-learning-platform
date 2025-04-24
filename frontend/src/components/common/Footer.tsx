import { Container, Row, Col } from "react-bootstrap"

const Footer = () => {
    return (
        <footer className="bg-light py-4 mt-5">
            <Container>
                <Row>
                    <Col md={6} className="text-center text-md-start">
                        <p className="mb-0">&copy; {new Date().getFullYear()} E-Learning Platform</p>
                    </Col>
                    <Col md={6} className="text-center text-md-end">
                        <p className="mb-0">Built with Spring Boot and React</p>
                    </Col>
                </Row>
            </Container>
        </footer>
    )
}

export default Footer
