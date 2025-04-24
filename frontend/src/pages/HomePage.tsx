import { Container, Row, Col, Card, Button } from "react-bootstrap"
import { Link } from "react-router-dom"

const HomePage = () => {
    return (
        <Container>
            <Row className="mb-5">
                <Col>
                    <div className="text-center py-5">
                        <h1 className="display-4 fw-bold">Welcome to E-Learning Platform</h1>
                        <p className="lead">
                            Access high-quality courses, track your progress, and enhance your learning experience
                        </p>
                        <div className="d-flex justify-content-center gap-3 mt-4">
                            <Button as={Link} to="/courses" variant="primary" size="lg">
                                Browse Courses
                            </Button>
                            <Button as={Link} to="/instructors" variant="outline-primary" size="lg">
                                Meet Our Instructors
                            </Button>
                        </div>
                    </div>
                </Col>
            </Row>

            <Row className="mb-5">
                <Col>
                    <h2 className="text-center mb-4">Platform Features</h2>
                </Col>
            </Row>

            <Row className="g-4 mb-5">
                <Col md={4}>
                    <Card className="h-100 shadow-sm">
                        <Card.Body className="text-center p-4">
                            <div className="mb-3">
                                <i className="bi bi-collection-play fs-1 text-primary"></i>
                            </div>
                            <Card.Title>Video Courses</Card.Title>
                            <Card.Text>Access a wide range of video courses on various topics to enhance your skills.</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card className="h-100 shadow-sm">
                        <Card.Body className="text-center p-4">
                            <div className="mb-3">
                                <i className="bi bi-graph-up fs-1 text-primary"></i>
                            </div>
                            <Card.Title>Progress Tracking</Card.Title>
                            <Card.Text>Track your learning progress and see how far you've come in your courses.</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={4}>
                    <Card className="h-100 shadow-sm">
                        <Card.Body className="text-center p-4">
                            <div className="mb-3">
                                <i className="bi bi-people fs-1 text-primary"></i>
                            </div>
                            <Card.Title>Expert Instructors</Card.Title>
                            <Card.Text>Learn from industry professionals with years of experience in their fields.</Card.Text>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row className="mb-5">
                <Col className="text-center">
                    <h2 className="mb-4">Ready to Start Learning?</h2>
                    <Button as={Link} to="/courses" variant="primary" size="lg">
                        Explore Courses
                    </Button>
                </Col>
            </Row>
        </Container>
    )
}

export default HomePage
